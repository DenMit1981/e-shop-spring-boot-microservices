package com.denmit.eshop.paymentservice.mapper;

import com.denmit.eshop.paymentservice.dto.request.BankTransferRequestDto;
import com.denmit.eshop.paymentservice.dto.request.CardPaymentRequestDto;
import com.denmit.eshop.paymentservice.dto.request.PaymentRequestDto;
import com.denmit.eshop.paymentservice.dto.response.*;
import com.denmit.eshop.paymentservice.kafka.model.PaymentRegister;
import com.denmit.eshop.paymentservice.model.Payment;
import com.denmit.eshop.paymentservice.model.enums.CardBrand;
import com.denmit.eshop.paymentservice.utils.DtoUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.time.LocalDate;
import java.util.Arrays;

@Mapper(componentModel = "spring", imports = {JsonNodeFactory.class, ObjectNode.class})
public interface PaymentMapper {

    PaymentResponseDto toPaymentDto(Payment entity, String payer);

    PaymentRegister toPaymentRegister(Payment entity, String payer);

    @Mapping(target = "cardNumber", source = "entity", qualifiedByName = "getCardNumber")
    @Mapping(target = "cardBrand", source = "entity", qualifiedByName = "getCardBrand")
    @Mapping(target = "cardHolderName", source = "entity", qualifiedByName = "getCardHolderName")
    @Mapping(target = "expiryDate", source = "entity", qualifiedByName = "getExpiryDate")
    @Mapping(target = "cvc", source = "entity", qualifiedByName = "getCvc")
    CardPaymentResponseDto toCardPaymentDto(Payment entity, MerchantCardPaymentDetailsResponseDto receiver, String payer);

    @Mapping(target = "fields", source = "dto", qualifiedByName = "mapFieldsToJsonNode")
    @Mapping(target = "allFieldsFilled", source = "dto", qualifiedByName = "areAllCardPaymentFieldsFilled")
    Payment toEntity(CardPaymentRequestDto dto);

    @Mapping(target = "senderBankAccount", source = "entity", qualifiedByName = "getSenderBankAccount")
    BankTransferResponseDto toBankTransferDto(Payment entity, MerchantBankTransferDetailsResponseDto receiver, String payer);

    @Mapping(target = "fields", source = "dto", qualifiedByName = "mapFieldsToJsonNode")
    @Mapping(target = "allFieldsFilled", source = "dto", qualifiedByName = "areAllBankTransferFieldsFilled")
    Payment toEntity(BankTransferRequestDto dto);

    @Named("mapFieldsToJsonNode")
    default ObjectNode mapFieldsToJsonNode(PaymentRequestDto dto) {
        ObjectNode fieldsNode = new ObjectMapper().createObjectNode();
        Arrays.stream(dto.getClass().getDeclaredFields())
                .filter(field -> {
                    field.setAccessible(true);
                    try {
                        return field.get(dto) != null && !field.get(dto).toString().isEmpty();
                    } catch (IllegalAccessException e) {
                        return false;
                    }
                })
                .forEach(field -> {
                    try {
                        fieldsNode.put(field.getName(), field.get(dto).toString());
                    } catch (IllegalAccessException e) {
                        // Handle exception
                    }
                });
        return fieldsNode;
    }

    @Named("getCardNumber")
    default String getCardNumber(Payment entity) {
        return getField(entity, "cardNumber");
    }

    @Named("getCardBrand")
    default CardBrand getCardBrand(Payment entity) {
        String field = getField(entity, "cardBrand");
        return field != null ? CardBrand.valueOf(field) : null;
    }

    @Named("getCardHolderName")
    default String getCardHolderName(Payment entity) {
        return getField(entity, "cardHolderName");
    }

    @Named("getExpiryDate")
    default LocalDate getExpiryDate(Payment entity) {
        String field = getField(entity, "expiryDate");
        return field != null ? LocalDate.parse(field) : null;
    }

    @Named("getCvc")
    default String getCvc(Payment entity) {
        return getField(entity, "cvc");
    }

    @Named("getSenderBankAccount")
    default String getSenderBankAccount(Payment entity) {
        return getField(entity, "senderBankAccount");
    }

    @Named("areAllCardPaymentFieldsFilled")
    default boolean areAllCardPaymentFieldsFilled(CardPaymentRequestDto requestDto) throws IllegalAccessException {
        return DtoUtils.areAllFieldsFilled(requestDto);
    }

    @Named("areAllBankTransferFieldsFilled")
    default boolean areAllBankTransferFieldsFilled(BankTransferRequestDto requestDto) throws IllegalAccessException {
        return DtoUtils.areAllFieldsFilled(requestDto);
    }

    private String getField(Payment entity, String field) {
        JsonNode node = entity.getFields().path(field);
        return node != null && !node.asText().isEmpty() ? node.asText() : null;
    }
}
