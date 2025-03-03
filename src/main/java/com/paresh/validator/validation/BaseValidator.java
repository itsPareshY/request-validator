package com.paresh.validator.validation;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class BaseValidator implements Validator {
    private Validator next;

    @Override
    public Map<String, String> validate(Object obj) {
        Map<String, String> errors = new HashMap<>();
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value = field.get(obj);
                if (value == null) {
                    errors.put(field.getName(), "Field cannot be null");
                } else if (value instanceof String) {
                    String strValue = (String) value;
                    if (strValue.length() < 2 || strValue.length() > 50) {
                        errors.put(field.getName(), "Length must be between 2 and 50 characters");
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        // Pass to next validator if exists
        if (next != null) {
            errors.putAll(next.validate(obj));
        }
        return errors;
    }

    @Override
    public void setNext(Validator next) {
        this.next = next;
    }
}
