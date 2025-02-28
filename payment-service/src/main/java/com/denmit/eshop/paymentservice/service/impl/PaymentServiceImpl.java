package com.denmit.eshop.paymentservice.service.impl;

import com.denmit.eshop.paymentservice.client.HistoryClient;
import com.denmit.eshop.paymentservice.client.OrderClient;
import com.denmit.eshop.paymentservice.client.UserClient;
import com.denmit.eshop.paymentservice.dto.request.BankTransferRequestDto;
import com.denmit.eshop.paymentservice.dto.request.CardPaymentRequestDto;
import com.denmit.eshop.paymentservice.dto.request.PaymentRequestDto;
import com.denmit.eshop.paymentservice.dto.response.*;
import com.denmit.eshop.paymentservice.exception.IncorrectPaymentException;
import com.denmit.eshop.paymentservice.exception.PaymentAlreadyExistsException;
import com.denmit.eshop.paymentservice.exception.PaymentNotFoundException;
import com.denmit.eshop.paymentservice.exception.ReceiverNotFoundException;
import com.denmit.eshop.paymentservice.kafka.service.KafkaService;
import com.denmit.eshop.paymentservice.mapper.PaymentMapper;
import com.denmit.eshop.paymentservice.mapper.ReceiverMapper;
import com.denmit.eshop.paymentservice.model.MerchantPaymentDetails;
import com.denmit.eshop.paymentservice.model.Payment;
import com.denmit.eshop.paymentservice.model.enums.PaymentMethod;
import com.denmit.eshop.paymentservice.model.enums.PaymentStatus;
import com.denmit.eshop.paymentservice.repository.PaymentRepository;
import com.denmit.eshop.paymentservice.repository.ReceiverRepository;
import com.denmit.eshop.paymentservice.service.EmailService;
import com.denmit.eshop.paymentservice.service.PaymentService;
import com.denmit.eshop.paymentservice.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final ReceiptService receiptService;
    private final EmailService emailService;
    private final KafkaService kafkaService;
    private final ReceiverRepository receiverRepository;
    private final PaymentMapper paymentMapper;
    private final ReceiverMapper receiverMapper;
    private final OrderClient orderClient;
    private final UserClient userClient;
    private final HistoryClient historyClient;

    @Override
    public PaymentResponseDto create(PaymentRequestDto paymentRequestDto, PaymentMethod paymentMethod,
                                     Long userId, Long orderId) {
        validatePayment(paymentRequestDto, orderId);

        Payment payment = savePayment(paymentRequestDto, paymentMethod, userId, orderId);
        ReceiptResponseDto receipt = receiptService.generate(userId, orderId);

        historyClient.saveHistoryForOrderPayment(orderId, payment.getPaymentNumber());
        emailService.sendReceiptMessage(orderId, receipt.getId(), userId);
        kafkaService.sendMessage(payment);

        return getPaymentDtoByPaymentMethod(paymentMethod, payment, userId);
    }

    @Override
    public PaymentResponseDto getByOrderId(Long orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId).orElseThrow(PaymentNotFoundException::new);

        return getPaymentInfo(payment);
    }

    @Override
    public PaymentResponseDto getById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(PaymentNotFoundException::new);

        return getPaymentInfo(payment);
    }

    @Override
    public PaymentResponseDto getByPaymentNumber(String paymentNumber) {
        Payment payment = paymentRepository.findByPaymentNumber(paymentNumber).orElseThrow(PaymentNotFoundException::new);

        return getPaymentInfo(payment);
    }

    @Transactional
    private Payment savePayment(PaymentRequestDto paymentRequestDto, PaymentMethod paymentMethod,
                                Long userId, Long orderId) {
        Payment payment = getPaymentByPaymentMethod(paymentMethod, paymentRequestDto);

        if (!payment.isAllFieldsFilled()) {
            payment.setStatus(PaymentStatus.FAILURE);
            throw new IncorrectPaymentException("Not all fields filled in");
        }

        payment.setUserId(userId);
        payment.setOrderId(orderId);
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentNumber(generatePaymentNumber());
        payment.setPaymentMethod(paymentMethod);
        payment.setMerchantPaymentDetails(findReceiverById(1L));
        payment.setDate(LocalDateTime.now());

        log.info("Payment № {} has just been made for order {}", payment.getPaymentNumber(), orderId);

        return paymentRepository.save(payment);
    }

    private Payment getPaymentByPaymentMethod(PaymentMethod paymentMethod, PaymentRequestDto paymentRequestDto) {
        return switch (paymentMethod) {
            case CARD_PAYMENT -> paymentMapper.toEntity((CardPaymentRequestDto) paymentRequestDto);
            case BANK_TRANSFER -> paymentMapper.toEntity((BankTransferRequestDto) paymentRequestDto);
        };
    }

    private PaymentResponseDto getPaymentDtoByPaymentMethod(PaymentMethod paymentMethod, Payment payment, Long senderId) {
        MerchantPaymentDetails receiver = findReceiverById(1L);
        MerchantCardPaymentDetailsResponseDto cardReceiveDto = receiverMapper.toCardPaymentDto(receiver);
        MerchantBankTransferDetailsResponseDto bankReceiveDto = receiverMapper.toBankTransferDto(receiver);
        String payer = userClient.getById(senderId).getName();

        return switch (paymentMethod) {
            case CARD_PAYMENT -> paymentMapper.toCardPaymentDto(payment, cardReceiveDto, payer);
            case BANK_TRANSFER -> paymentMapper.toBankTransferDto(payment, bankReceiveDto, payer);
        };
    }

    private MerchantPaymentDetails findReceiverById(Long receiverId) {
        return receiverRepository.findById(receiverId).orElseThrow(ReceiverNotFoundException::new);
    }

    private String generatePaymentNumber() {
        long count = paymentRepository.count();
        String prefix = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-"));
        return prefix + String.format("%05d", count + 1);
    }

    private PaymentResponseDto getPaymentInfo(Payment payment) {
        String payer = userClient.getById(payment.getUserId()).getName();

        return paymentMapper.toPaymentDto(payment, payer);
    }

    private void validatePayment(PaymentRequestDto paymentRequestDto, Long orderId) {
        OrderHistoryResponseDto order = orderClient.getByIdForHistory(orderId);
        BigDecimal roundedPrice = paymentRequestDto.getAmount().setScale(2, RoundingMode.HALF_UP);

        if (paymentRepository.existsByOrderId(orderId)) {
            throw new PaymentAlreadyExistsException();
        }

        if (roundedPrice.compareTo(order.getTotalPrice()) != 0) {
            throw new IncorrectPaymentException("Wrong amount entered");
        }
    }
}
