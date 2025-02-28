package com.denmit.eshop.commentservice.mapper;

import com.denmit.eshop.commentservice.dto.request.CommentRequestDto;
import com.denmit.eshop.commentservice.dto.response.CommentResponseDto;
import com.denmit.eshop.commentservice.dto.response.CommentUserResponseDto;
import com.denmit.eshop.commentservice.model.Comment;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toEntity(CommentRequestDto commentDto);

    CommentResponseDto toDto(Comment entity, String user);

    CommentUserResponseDto toCommentUserDto(Comment entity, String user);

    default List<CommentResponseDto> toDtos(List<Comment> comments, String user) {
        return comments.stream()
                .map(entity -> toDto(entity, user))
                .collect(Collectors.toList());
    }
}
