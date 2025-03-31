package com.remitly.axan18.swift_api.exceptions;

public class InvalidCountryCodeException extends RuntimeException{
    public InvalidCountryCodeException(String message){
        super(message);
    }
}
