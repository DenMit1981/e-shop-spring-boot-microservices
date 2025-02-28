package com.denmit.eshop.feedbackservice.exception;

public class FeedbackNotFoundException extends RuntimeException {

    public FeedbackNotFoundException() {
        super("Feedback not found");
    }
}
