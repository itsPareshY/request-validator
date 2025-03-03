package com.paresh.validator.validation;

import com.paresh.validator.model.UserRequest;
import java.util.HashMap;
import java.util.Map;

public class AgeValidator implements Validator {
    private Validator next;

    @Override
    public Map<String, String> validate(Object obj) {
        UserRequest request = (UserRequest) obj;
        Map<String, String> errors = new HashMap<>();
        Integer age = request.getAge();
        if (age != null && (age < 18 || age > 100)) {
            errors.put("age", "Age must be between 18 and 100");
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
