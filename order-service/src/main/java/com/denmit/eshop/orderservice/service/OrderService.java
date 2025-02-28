package com.denmit.eshop.orderservice.service;

import com.denmit.eshop.orderservice.dto.response.OrderAdminResponseDto;
import com.denmit.eshop.orderservice.dto.response.OrderHistoryResponseDto;
import com.denmit.eshop.orderservice.dto.response.OrderResponseDto;
import com.denmit.eshop.orderservice.dto.response.OrderUserResponseDto;

import java.util.List;

public interface OrderService {

    OrderResponseDto save(Long userId);

    List<OrderAdminResponseDto> getAllForAdmin(String sortField, String sortDirection, int pageSize, int pageNumber);

    OrderResponseDto getByIdForBuyer(Long orderId);

    OrderAdminResponseDto getByIdForAdmin(Long orderId);

    OrderHistoryResponseDto getByIdForHistory(Long orderId);

    OrderUserResponseDto getOrderUserById(Long orderId);

    void checkOrderExistenceById(Long orderId);
}
