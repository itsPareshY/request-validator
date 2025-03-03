package com.paresh.validator.validation;

import com.paresh.validator.model.UserRequest;
import java.util.HashMap;
import java.util.Map;

public class EmailValidator implements Validator {
    @Override
    public Map<String, String> validate(Object obj) {
        UserRequest request = (UserRequest) obj;
        Map<String, String> errors = new HashMap<>();
        String email = request.getEmail();
        if (email != null && !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            errors.put("email", "Invalid email format");
        }
        return errors;
    }
}
