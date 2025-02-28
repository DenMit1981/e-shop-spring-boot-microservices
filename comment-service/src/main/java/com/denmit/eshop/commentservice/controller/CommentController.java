package com.denmit.eshop.commentservice.controller;

import com.denmit.eshop.commentservice.dto.request.CommentRequestDto;
import com.denmit.eshop.commentservice.dto.response.CommentResponseDto;
import com.denmit.eshop.commentservice.dto.response.CommentUserResponseDto;
import com.denmit.eshop.commentservice.security.provider.UserProvider;
import com.denmit.eshop.commentservice.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comments")
@CrossOrigin(origins = "http://localhost:3000")
@Tag(name = "Comment controller")
public class CommentController {

    private final CommentService commentService;
    private final UserProvider userProvider;

    @PostMapping("/order/{orderId}")
    @Operation(summary = "Add new comment to order by buyer")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto add(@RequestBody @Valid CommentRequestDto commentRequestDto,
                                  @PathVariable Long orderId) {
        return commentService.add(commentRequestDto, orderId, Long.valueOf(userProvider.getUserId()));
    }

    @GetMapping("/order/{orderId}")
    @Operation(summary = "Get all order comments")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentResponseDto> getAllByOrderId(@PathVariable Long orderId,
                                                    @RequestParam(defaultValue = "default") String buttonValue) {
        return commentService.getAllByOrderId(orderId, buttonValue);
    }

    @GetMapping("/{commentId}")
    @Operation(summary = "Get comment by ID")
    @ResponseStatus(HttpStatus.OK)
    public CommentUserResponseDto getById(@PathVariable Long commentId) {
        return commentService.getById(commentId);
    }

    @DeleteMapping("/{commentId}")
    @Operation(summary = "Delete comment by ID")
    @ResponseStatus(HttpStatus.OK)
    public void deleteById(@PathVariable Long commentId) {
        commentService.deleteById(commentId, Long.valueOf(userProvider.getUserId()));
    }
}
