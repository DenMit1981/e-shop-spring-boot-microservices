package com.denmit.eshop.historyservice.mapper;

import com.denmit.eshop.historyservice.dto.HistoryResponseDto;
import com.denmit.eshop.historyservice.model.History;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface HistoryMapper {

    HistoryResponseDto toDto(History entity, String user);

    default List<HistoryResponseDto> toDtos(List<History> history, String user) {
        return history.stream()
                .map(entity -> toDto(entity, user))
                .collect(Collectors.toList());
    }
}
