package com.paresh.validator.validation;

import com.paresh.validator.model.UserRequest;
import com.paresh.validator.validation.Validator;
import java.util.HashMap;
import java.util.Map;

public class AgeValidator implements Validator {
    @Override
    public Map<String, String> validate(Object obj) {
        UserRequest request = (UserRequest) obj;
        Map<String, String> errors = new HashMap<>();
        Integer age = request.getAge();
        if (age != null && (age < 18 || age > 100)) {
            errors.put("age", "Age must be between 18 and 100");
        }
        return errors;
    }
}
