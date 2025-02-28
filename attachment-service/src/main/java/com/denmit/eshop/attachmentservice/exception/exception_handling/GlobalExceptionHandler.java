package com.denmit.eshop.attachmentservice.exception.exception_handling;

import com.denmit.eshop.attachmentservice.exception.*;
import feign.FeignException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Object> resolveHandle(MethodArgumentNotValidException e) {
        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getAllErrors()
                .forEach(error -> {
                    String fieldName = ((FieldError) error).getField();
                    String errorMessage = error.getDefaultMessage();
                    errors.put(fieldName, errorMessage);
                });

        List<Map.Entry<String, String>> listErrors = new ArrayList<>(errors.entrySet());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(listErrors);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionInfo handleException(FileNotFoundException e) {
        return getExceptionInfo(e);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionInfo handleException(FileAlreadyExistsException e) {
        return getExceptionInfo(e);
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionInfo handleInvalidFileTypeException(InvalidFileTypeException e) {
        return getExceptionInfo(e);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionInfo handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        return getExceptionInfo(new FileSizeExceededException());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ExceptionInfo handleException(AttachmentAccessException e) {
        return getExceptionInfo(e);
    }

    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionInfo handleFeignException(FeignException ex) {
        ExceptionInfo info = new ExceptionInfo();
        String message = extractErrorMessage(ex);
        info.setInfo(message);
        return info;
    }

    private String extractErrorMessage(FeignException ex) {
        try {
            String jsonString = ex.contentUTF8();
            JSONObject jsonObject = new JSONObject(jsonString);
            return jsonObject.getString("info");
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ExceptionInfo getExceptionInfo(Exception e) {
        ExceptionInfo info = new ExceptionInfo();
        info.setInfo(e.getMessage());
        return info;
    }
}
