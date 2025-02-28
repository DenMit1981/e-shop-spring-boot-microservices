package com.denmit.eshop.orderservice.service.impl;

import com.denmit.eshop.orderservice.client.HistoryClient;
import com.denmit.eshop.orderservice.client.ProductClient;
import com.denmit.eshop.orderservice.client.UserClient;
import com.denmit.eshop.orderservice.dto.response.*;
import com.denmit.eshop.orderservice.exception.OrderNotFoundException;
import com.denmit.eshop.orderservice.exception.OrderNotPlacedException;
import com.denmit.eshop.orderservice.kafka.service.KafkaService;
import com.denmit.eshop.orderservice.mapper.OrderMapper;
import com.denmit.eshop.orderservice.model.Cart;
import com.denmit.eshop.orderservice.model.Order;
import com.denmit.eshop.orderservice.repository.CartRepository;
import com.denmit.eshop.orderservice.repository.OrderRepository;
import com.denmit.eshop.orderservice.service.EmailService;
import com.denmit.eshop.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private static final String ORDER_NOT_PLACED = "Your order not placed yet";
    private static final String ORDER_NOT_FOUND = "Order with id %s not found";

    private final ProductClient productClient;
    private final UserClient userClient;
    private final HistoryClient historyClient;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final KafkaService kafkaService;
    private final EmailService emailService;

    @Override
    public OrderResponseDto save(Long userId) {
        Order order = createOrder(userId);

        historyClient.saveHistoryForCreatedOrder(order.getId());
        sendOrderDetailsMessage(order, userId);
        kafkaService.sendMessage(order);

        return getOrderResponseDtoFromEntity(order);
    }

    @Override
    public List<OrderAdminResponseDto> getAllForAdmin(String sortField, String sortDirection, int pageSize, int pageNumber) {
        Comparator<OrderAdminResponseDto> comparator = Comparator.comparing(OrderAdminResponseDto::getUser);

        if ("user".equals(sortField)) {
            if ("desc".equals(sortDirection)) {
                comparator = comparator.reversed().thenComparing(OrderAdminResponseDto::getId);
            } else {
                comparator = comparator.thenComparing(OrderAdminResponseDto::getId);
            }

            return orderRepository.findAll().stream()
                    .map(this::getOrderAdminResponseDtoFromEntity)
                    .sorted(comparator)
                    .skip((long) pageNumber * pageSize)
                    .limit(pageSize)
                    .collect(Collectors.toList());
        } else {
            PageRequest pageable = PageRequest.of(pageNumber, pageSize, Sort.by(Sort.Direction.fromString(sortDirection), sortField));
            return orderRepository.findAll(pageable).map(this::getOrderAdminResponseDtoFromEntity).toList();
        }
    }

    @Override
    public OrderResponseDto getByIdForBuyer(Long orderId) {
        return getOrderResponseDtoFromEntity(findById(orderId));
    }

    @Override
    public OrderAdminResponseDto getByIdForAdmin(Long orderId) {
        return getOrderAdminResponseDtoFromEntity(findById(orderId));
    }

    @Override
    public OrderHistoryResponseDto getByIdForHistory(Long orderId) {
        return orderMapper.toOrderHistoryDto(findById(orderId));
    }

    @Override
    public OrderUserResponseDto getOrderUserById(Long orderId) {
        return orderMapper.toOrderUserDto(findById(orderId));
    }

    @Override
    public void checkOrderExistenceById(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new OrderNotFoundException(String.format(ORDER_NOT_FOUND, orderId));
        }
    }

    @Transactional
    private Order createOrder(Long userId) {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(() -> new OrderNotPlacedException(ORDER_NOT_PLACED));
        if (cart.getProductsIds().isEmpty()) {
            throw new OrderNotPlacedException(ORDER_NOT_PLACED);
        }

        Order order = new Order();
        order.setUserId(userId);
        order.getProductsIds().addAll(cart.getProductsIds());

        OrderResponseDto orderResponseDto = getOrderResponseDtoFromEntity(order);

        BigDecimal totalPrice = getTotalPrice(orderResponseDto.getGoods());
        String description = getOrderedGoodsDescription(orderResponseDto.getGoods());

        order.setTotalPrice(totalPrice);
        order.setDescription(description);

        cart.setProductsIds(new ArrayList<>());
        order.getProductsIds().forEach(productClient::removeOrderedProduct);

        return orderRepository.save(order);
    }

    private OrderResponseDto getOrderResponseDtoFromEntity(Order order) {
        return createOrderResponseDto(order, orderMapper.toDto(order));
    }

    private OrderAdminResponseDto getOrderAdminResponseDtoFromEntity(Order order) {
        OrderAdminResponseDto orderDto = orderMapper.toAdminDto(order, userClient.getById(order.getUserId()).getName());

        return createOrderResponseDto(order, orderDto);
    }

    private <T extends OrderResponseDto> T createOrderResponseDto(Order order, T orderDto) {
        List<ProductUnitResponseDto> goods = order.getProductsIds().stream()
                .map(productClient::getUnitById)
                .toList();

        List<CartProductResponseDto> orderGoods = productClient.getProductCategoriesFromUnits(goods).stream()
                .peek(product -> {
                    long quantity = order.getProductsIds().stream()
                            .filter(id -> {
                                var unit = productClient.getUnitById(id);
                                return unit.getTitle().equals(product.getTitle()) &&
                                        unit.getPrice().equals(product.getPrice());
                            })
                            .count();
                    product.setQuantity(quantity);
                    product.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
                })
                .toList();

        orderDto.setGoods(orderGoods);
        return orderDto;
    }

    private Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(String.format(ORDER_NOT_FOUND, id)));
    }

    private String getOrderedGoodsDescription(List<CartProductResponseDto> goods) {
        return goods.stream()
                .map(product -> String.format("%d) %s: $%s x %d = $%s\n",
                        goods.indexOf(product) + 1,
                        product.getTitle(), product.getPrice(), product.getQuantity(), product.getTotalPrice()))
                .collect(Collectors.joining()) + "\nTotal: $ " + getTotalPrice(goods);
    }

    private BigDecimal getTotalPrice(List<CartProductResponseDto> goods) {
        return goods.stream()
                .map(CartProductResponseDto::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void sendOrderDetailsMessage(Order order, Long userId) {
        OrderResponseDto orderDto = getByIdForBuyer(order.getId());
        String description = getOrderedGoodsDescription(orderDto.getGoods());

        emailService.sendOrderDetailsMessage(order.getId(), description, userId);
    }
}
