package com.denmit.eshop.paymentservice.utils;

import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class DtoUtils {

    public static boolean areAllFieldsFilled(Object dto) throws IllegalAccessException {
        return areAllFieldsFilled(dto, dto.getClass());
    }

    public static boolean areAllFieldsFilled(Object dto, Class<?> clazz) throws IllegalAccessException {
        if (clazz == null || clazz == Object.class) {
            return true;
        }

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.get(dto) == null) {
                return false;
            }
        }

        return areAllFieldsFilled(dto, clazz.getSuperclass());
    }
}

