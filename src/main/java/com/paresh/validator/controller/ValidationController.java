package com.paresh.validator.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.paresh.validator.model.UserRequest;
import com.paresh.validator.validation.Validator;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class ValidationController {

    private static final Logger logger = LoggerFactory.getLogger(ValidationController.class);
    private static final String VALIDATORS_FILE = "src/main/resources/validators.json";

    private Validator setupValidatorChain() {
        Validator firstValidator = null;
        Validator previousValidator = null;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(new File(VALIDATORS_FILE));
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
            logger.error("Error setting up validator chain", e);
            throw new RuntimeException("Failed to initialize validators", e);
        }

        return firstValidator;
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateRequest(@Valid @RequestBody UserRequest request) {
        Validator validator = setupValidatorChain();
        Map<String, String> validationErrors = validator.validate(request);

        if (!validationErrors.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("errors", validationErrors));
        }
        return ResponseEntity.ok(Map.of("message", "Request validation successful for user: " + request.getName()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(Map.of("errors", errors));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        logger.error("An unexpected error occurred", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "An internal server error occurred"));
    }
}