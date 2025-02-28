package com.denmit.eshop.commentservice.service;

import com.denmit.eshop.commentservice.dto.request.CommentRequestDto;
import com.denmit.eshop.commentservice.dto.response.CommentResponseDto;
import com.denmit.eshop.commentservice.dto.response.CommentUserResponseDto;

import java.util.List;

public interface CommentService {

    CommentResponseDto add(CommentRequestDto commentDto, Long orderId, Long userId);

    List<CommentResponseDto> getAllByOrderId(Long orderId, String buttonValue);

    CommentUserResponseDto getById(Long commentId);

    void deleteById(Long commentId, Long userId);
}
