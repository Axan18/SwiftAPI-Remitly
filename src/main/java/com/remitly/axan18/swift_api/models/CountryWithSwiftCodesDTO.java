package com.remitly.axan18.swift_api.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class CountryWithSwiftCodesDTO {
    private String countryISO2;
    private String countryName;
    private List<BankDTO> swiftCodes;
}