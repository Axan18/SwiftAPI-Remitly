package com.remitly.axan18.swift_api.bootstrap;

import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
public class BankValidator {

    public boolean isValidISO2(String ccISO2) {
        if (ccISO2 == null || ccISO2.length() != 2) {
            return false;
        }
        String[] isoCountries = Locale.getISOCountries();
        for (String countryCode : isoCountries) {
            if (countryCode.equalsIgnoreCase(ccISO2)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidSwift(String s) {
        return s.length() == 11 && s.matches("^[A-Za-z0-9]{11}$");
    }
}
