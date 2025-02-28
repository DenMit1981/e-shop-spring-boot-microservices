package com.denmit.eshop.commentservice.service.impl;

import com.denmit.eshop.commentservice.client.HistoryClient;
import com.denmit.eshop.commentservice.client.OrderClient;
import com.denmit.eshop.commentservice.client.UserClient;
import com.denmit.eshop.commentservice.dto.request.CommentRequestDto;
import com.denmit.eshop.commentservice.dto.response.CommentResponseDto;
import com.denmit.eshop.commentservice.dto.response.CommentUserResponseDto;
import com.denmit.eshop.commentservice.dto.response.OrderUserResponseDto;
import com.denmit.eshop.commentservice.exception.CommentAccessException;
import com.denmit.eshop.commentservice.exception.CommentNotFoundException;
import com.denmit.eshop.commentservice.mapper.CommentMapper;
import com.denmit.eshop.commentservice.model.Comment;
import com.denmit.eshop.commentservice.repository.CommentRepository;
import com.denmit.eshop.commentservice.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final OrderClient orderClient;
    private final UserClient userClient;
    private final HistoryClient historyClient;

    @Override
    public CommentResponseDto add(CommentRequestDto commentDto, Long orderId, Long userId) {
        OrderUserResponseDto order = checkAccess(orderId, userId);

        Comment comment = saveComment(commentDto, order, orderId);

        historyClient.saveHistoryForAddedComment(comment.getId(), orderId);

        return commentMapper.toDto(comment, userClient.getById(order.getUserId()).getName());
    }

    @Override
    public List<CommentResponseDto> getAllByOrderId(Long orderId, String buttonValue) {

        OrderUserResponseDto order = orderClient.getOrderUserById(orderId);

        List<Comment> comments = buttonValue.equals("Show All")
                ? commentRepository.findByOrderIdOrderByDate(orderId)
                : commentRepository.findByOrderId(orderId, PageRequest.of(0, 5, Sort.by(Sort.Direction.DESC, "date"))).toList();

        log.info("{} comments for order {}: {}", buttonValue.equals("Show All") ? "All" : "Last 5", orderId, comments);

        return commentMapper.toDtos(comments, userClient.getById(order.getUserId()).getName());
    }

    @Override
    public CommentUserResponseDto getById(Long commentId) {
        Comment comment = findById(commentId);

        return commentMapper.toCommentUserDto(comment, userClient.getById(comment.getUserId()).getName());
    }

    @Override
    public void deleteById(Long commentId, Long userId) {
        Comment comment = findById(commentId);

        checkAccess(comment.getOrderId(), userId);
        historyClient.saveHistoryForRemovedComment(commentId, comment.getOrderId());

        removeComment(comment);
    }

    @Transactional
    private Comment saveComment(CommentRequestDto commentDto, OrderUserResponseDto order, Long orderId) {
        Comment comment = commentMapper.toEntity(commentDto);

        comment.setOrderId(orderId);
        comment.setUserId(order.getUserId());
        comment.setDate(LocalDateTime.now());

        log.info("New commit has just been added to order {}: {}", orderId, comment.getText());

        return commentRepository.save(comment);
    }

    @Transactional
    private void removeComment(Comment comment) {
        commentRepository.deleteById(comment.getId());

        log.info("Comment {} has just been deleted from order {}", comment.getId(), comment.getOrderId());
    }

    private Comment findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(CommentNotFoundException::new);
    }

    private OrderUserResponseDto checkAccess(Long orderId, Long userId) {
        OrderUserResponseDto order = orderClient.getOrderUserById(orderId);

        if (!order.getUserId().equals(userId)) {
            throw new CommentAccessException();
        }

        return order;
    }
}
