package com.remitly.axan18.swift_api.mappers;

import org.mapstruct.Mapper;

import java.util.Locale;

@Mapper(componentModel = "spring")
public interface CountryMapper {
    default String iso2ToCountryName(String iso2Code){
        if (iso2Code == null || iso2Code.length() != 2) {
            return "";
        }
        return Locale.of("", iso2Code).getDisplayCountry(Locale.ENGLISH).toUpperCase();
    }
}
