package com.denmit.eshop.feedbackservice.mapper;

import com.denmit.eshop.feedbackservice.dto.request.FeedbackRequestDto;
import com.denmit.eshop.feedbackservice.dto.response.FeedbackInfoResponseDto;
import com.denmit.eshop.feedbackservice.dto.response.FeedbackMessageResponseDto;
import com.denmit.eshop.feedbackservice.dto.response.FeedbackResponseDto;
import com.denmit.eshop.feedbackservice.model.Feedback;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {

    Feedback toEntity(FeedbackRequestDto feedbackDto);

    FeedbackResponseDto toDto(Feedback entity, String user);

    FeedbackMessageResponseDto toMessageDto(Feedback entity);

    FeedbackInfoResponseDto toInfoDto(Feedback entity, String user);

    default List<FeedbackInfoResponseDto> toDtos(List<Feedback> feedbacks, String user) {
        return feedbacks.stream()
                .map(entity -> toInfoDto(entity, user))
                .collect(Collectors.toList());
    }
}
