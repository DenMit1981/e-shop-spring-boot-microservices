package com.denmit.eshop.commentservice.exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException() {
        super("Comment not found");
    }
}
