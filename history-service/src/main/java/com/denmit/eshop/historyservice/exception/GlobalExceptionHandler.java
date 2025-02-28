package com.denmit.eshop.historyservice.exception;

import feign.FeignException;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

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
}
