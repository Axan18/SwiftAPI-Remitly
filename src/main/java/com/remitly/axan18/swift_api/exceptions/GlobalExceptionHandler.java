package com.remitly.axan18.swift_api.exceptions;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.IllformedLocaleException;

@RestControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleNotFound(EntityNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(CannotDeleteEntityException.class)
    public ResponseEntity<String> handleDatabaseError(CannotDeleteEntityException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<String> handleDuplicateKey(DuplicateKeyException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(ValidationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred: " + e.getMessage());
    }
    @ExceptionHandler(IllformedLocaleException.class)
    public ResponseEntity<String> handleIllformedLocaleException(IllformedLocaleException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("\""+e.getMessage() +"\""+
                " is not a valid country code. Country code should have 2 uppercase letters corresponding to specific country in " +
                "ISO 3166-1 alpha-2 standard.");
    }
    @ExceptionHandler(InvalidSwiftCodeException.class)
    public ResponseEntity<String> handleInvalidSwiftCodeException(InvalidSwiftCodeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid SWIFT code:"+e.getMessage()+
                ". SWIFT code should have 11 characters and contain only uppercase letters and digits.");
    }
}
