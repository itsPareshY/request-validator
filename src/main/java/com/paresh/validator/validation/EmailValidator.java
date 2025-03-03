package com.paresh.validator.validation;

import com.paresh.validator.model.UserRequest;
import java.util.HashMap;
import java.util.Map;

public class EmailValidator implements Validator {
    private Validator next;

    @Override
    public Map<String, String> validate(Object obj) {
        UserRequest request = (UserRequest) obj;
        Map<String, String> errors = new HashMap<>();
        String email = request.getEmail();
        if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errors.put("email", "Invalid email format");
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
