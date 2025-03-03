package com.paresh.validator.controller;

import com.paresh.validator.model.UserRequest;
import com.paresh.validator.validation.Validator;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@PropertySource("classpath:application.properties")
@RestController
@RequestMapping("/api/v1")
public class ValidationController {

    @Value("${validators}")
    private String[] validatorsConfig;

    private List<Validator> validators;

    private void loadValidators() {
        validators = new ArrayList<>();
        for (String validatorName : validatorsConfig) {
            try {
                Class<?> clazz = Class.forName(validatorName);
                Validator validator = (Validator) clazz.getDeclaredConstructor().newInstance();
                validators.add(validator);
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Validator> getValidators() {
        if (validators == null) {
            loadValidators();
        }
        return validators;
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, String>> validateRequest(@Valid @RequestBody UserRequest request) {
        Map<String, String> errors = new HashMap<>();
        List<Validator> validators = getValidators();

        for (Validator validator : validators) {
            errors.putAll(validator.validate(request));
        }

        if (!errors.isEmpty()) {
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(Map.of("message", "Request validation successful for user: " + request.getName()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
