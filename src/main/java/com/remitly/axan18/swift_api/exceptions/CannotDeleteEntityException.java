package com.remitly.axan18.swift_api.exceptions;

public class CannotDeleteEntityException extends RuntimeException {
    public CannotDeleteEntityException(String message) {
        super(message);
    }

    public CannotDeleteEntityException(String message, Throwable cause) {
        super(message, cause);
    }
}