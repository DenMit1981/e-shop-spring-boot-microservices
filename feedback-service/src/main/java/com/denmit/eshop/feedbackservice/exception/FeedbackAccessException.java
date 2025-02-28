package com.denmit.eshop.feedbackservice.exception;

public class FeedbackAccessException extends RuntimeException {

    public FeedbackAccessException() {
        super("You can leave or delete feedback only to your order");
    }
}
