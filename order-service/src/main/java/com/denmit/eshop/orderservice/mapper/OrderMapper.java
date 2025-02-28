package com.denmit.eshop.orderservice.mapper;

import com.denmit.eshop.orderservice.dto.response.OrderAdminResponseDto;
import com.denmit.eshop.orderservice.dto.response.OrderHistoryResponseDto;
import com.denmit.eshop.orderservice.dto.response.OrderResponseDto;
import com.denmit.eshop.orderservice.dto.response.OrderUserResponseDto;
import com.denmit.eshop.orderservice.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderResponseDto toDto(Order entity);

    @Mapping(target = "id", source = "entity.id")
    OrderAdminResponseDto toAdminDto(Order entity, String user);

    OrderHistoryResponseDto toOrderHistoryDto(Order order);

    OrderUserResponseDto toOrderUserDto(Order order);
}
