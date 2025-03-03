package com.paresh.validator.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ValidationChain {
    private final List<Validator> validators = new ArrayList<>();

    public void addValidator(Validator validator) {
        validators.add(validator);
    }

    public Map<String, String> validate(Object obj) {
        Map<String, String> errors = new HashMap<>();
        for (Validator validator : validators) {
            errors.putAll(validator.validate(obj));
        }
        return errors;
    }
}
