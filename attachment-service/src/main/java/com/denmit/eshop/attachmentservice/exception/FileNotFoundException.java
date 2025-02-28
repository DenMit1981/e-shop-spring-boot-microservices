package com.denmit.eshop.attachmentservice.exception;

public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException() {
        super("File not found");
    }
}
