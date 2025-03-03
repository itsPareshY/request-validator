package com.paresh.validator.validation;

import java.util.Map;

public interface Validator {
    Map<String, String> validate(Object obj);
}
