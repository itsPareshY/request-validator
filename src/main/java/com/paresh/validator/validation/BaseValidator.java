package com.paresh.validator.validation;

import java.lang.reflect.Field;
import javax.validation.constraints.Size;
import com.paresh.validator.annotations.FreeText;
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
                    Size sizeAnnotation = field.getAnnotation(Size.class);
                    FreeText freeTextAnnotation = field.getAnnotation(FreeText.class);

                    if (freeTextAnnotation != null) {
                        // Skip validation for FreeText fields
                        continue;
                    }

                    if (sizeAnnotation != null) {
                        // Validate based on @Size annotation
                        int maxSize = sizeAnnotation.max();
                        if (strValue.length() > maxSize) {
                            throw new ValidationException("Field " + field.getName() + " exceeds max length of " + maxSize);
                        }
                    } else {
                        // Default validation for fields without @Size or @FreeText
                        if (strValue.length() > 255) {
                            throw new ValidationException("Field " + field.getName() + " exceeds default max length of 255");
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (ValidationException e) {
                errors.put(field.getName(), e.getMessage());
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

class ValidationException extends Exception {
    public ValidationException(String message) {
        super(message);
    }
}
