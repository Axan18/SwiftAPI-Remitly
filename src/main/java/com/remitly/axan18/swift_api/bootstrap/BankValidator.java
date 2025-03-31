package com.remitly.axan18.swift_api.bootstrap;

import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Set;

@Component
public class BankValidator{

    private final String[] iso2Countries = Locale.getISOCountries();
    public boolean isValidISO2(String ccISO2, String countryName) {
        if (ccISO2 == null || ccISO2.length() != 2) {
            return false;
        }
        Locale.Builder locBuilder = new Locale.Builder();
        for (String countryCode : iso2Countries) {
            if (countryCode.equalsIgnoreCase(ccISO2)) {// check if given iso country code is valid
                Locale locale = locBuilder.setRegion(ccISO2).build();
                if(locale.getDisplayCountry().equalsIgnoreCase(countryName)) // check if given country name corresponds to correct one
                    return true;
            }
        }
        return false;
    }

    public boolean isValidSwift(String s) {
        return s.length() == 11 && s.matches("^[A-Za-z0-9]{11}$");
    }
    public boolean isValidHeadquarter(String swiftCode, boolean isHeadquarter){
        return isHeadquarter == swiftCode.endsWith("XXX");
    }

    public boolean areValidColumns(Set<String> columns){
        Set<String> correctColumns = Set.of("COUNTRY ISO2 CODE","SWIFT CODE", "CODE TYPE", "NAME", "ADDRESS", "TOWN NAME", "COUNTRY NAME", "TIME ZONE");
        return columns.size() == correctColumns.size() && columns.containsAll(correctColumns);
    }
}
