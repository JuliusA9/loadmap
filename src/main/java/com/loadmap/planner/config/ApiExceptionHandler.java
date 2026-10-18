package com.loadmap.planner.config;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

// Runs whenever ANY controller throws one of these exceptions
@RestControllerAdvice
public class ApiExceptionHandler {
    // A @Valid rule failed, e.g. blank title or 500% weight
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleInvalid(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .findFirst()
                .orElse("Invalid input.");
        return ResponseEntity.badRequest().body(Map.of("error", message));
    }
    
    // The JSON itself was broken, e.g. a date like "tomorrow" or a type like "BANANA"
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleUnreadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", "The request was missing or badly formatted."));
    }
}