package com.paresh.validator.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paresh.validator.model.UserRequest;
import com.paresh.validator.validation.Validator;
import java.io.File;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ValidationController {

    private Validator setupValidatorChain() {
        Validator firstValidator = null;
        Validator previousValidator = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File("src/main/resources/validators.json"));
            JsonNode validatorsNode = rootNode.path("validators");
    
            for (JsonNode validatorNode : validatorsNode) {
                String validatorName = validatorNode.asText();
                Class<?> clazz = Class.forName(validatorName);
                Validator validator = (Validator) clazz.getDeclaredConstructor().newInstance();
                if (firstValidator == null) {
                    firstValidator = validator;
                }
                if (previousValidator != null) {
                    previousValidator.setNext(validator);
                }
                previousValidator = validator;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return firstValidator;
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, String>> validateRequest(@Valid @RequestBody UserRequest request) {
        Validator validator = setupValidatorChain();

        // Validate the request using the chain
        Map<String, String> errors = validator.validate(request);

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
