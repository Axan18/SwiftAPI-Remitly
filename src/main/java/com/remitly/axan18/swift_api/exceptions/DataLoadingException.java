package com.remitly.axan18.swift_api.exceptions;

public class DataLoadingException extends Exception{
    public DataLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
    public DataLoadingException(String message) {
        super(message);
    }
}
