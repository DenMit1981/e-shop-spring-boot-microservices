package com.denmit.eshop.commentservice.exception;

public class CommentAccessException extends RuntimeException {

    public CommentAccessException() {
        super("You can add or delete comment only to your order");
    }
}
