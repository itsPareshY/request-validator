package com.paresh.validator.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.paresh.validator.model.UserRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ValidationController.class)
public class ValidationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

//    @Test
//    public void whenValidInput_thenReturns200() throws Exception {
//        UserRequest request = new UserRequest();
//        request.setName("John Doe");
//        request.setEmail("john@example.com");
//        request.setAge(25);
//        request.setPhoneNumber("+1234567890");
//
//        mockMvc.perform(post("/api/v1/validate")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Request validation successful for user: John Doe"));
//    }

    @Test
    public void whenValidInput_thenReturns200() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setAge(25);
        request.setPhoneNumber("+1234567890");

        mockMvc.perform(post("/api/v1/validate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Request validation successful for user: John Doe"));
    }

    @Test
    public void whenInvalidEmail_thenReturns400() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("John Doe");
        request.setEmail("invalid-email");  // Invalid email
        request.setAge(25);
        request.setPhoneNumber("+1234567890");

        mockMvc.perform(post("/api/v1/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.email").value("Invalid email format"));
    }

    @Test
    public void whenAgeTooYoung_thenReturns400() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setAge(15);  // Too young
        request.setPhoneNumber("+1234567890");

        mockMvc.perform(post("/api/v1/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.age").value("Age must be at least 18"));
    }

    @Test
    public void whenInvalidPhoneNumber_thenReturns400() throws Exception {
        UserRequest request = new UserRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setAge(25);
        request.setPhoneNumber("invalid-phone");  // Invalid phone

        mockMvc.perform(post("/api/v1/validate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.phoneNumber").value("Invalid phone number format"));
    }
}
