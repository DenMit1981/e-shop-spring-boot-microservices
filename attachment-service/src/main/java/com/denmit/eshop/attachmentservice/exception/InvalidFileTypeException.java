package com.denmit.eshop.attachmentservice.exception;

public class InvalidFileTypeException extends RuntimeException {

    private static final String DOWNLOADABLE_FILE_FORMAT_ERROR_MESSAGE = "The selected file type is not allowed. Please select a file of " +
            "one of the following types: pdf, png, doc, docx, jpg, jpeg.";

    public InvalidFileTypeException() {
        super(DOWNLOADABLE_FILE_FORMAT_ERROR_MESSAGE);
    }
}
