package com.remitly.axan18.swift_api.models;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewBankDTO {
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private Boolean isHeadquarter;
    private String swiftCode;
}
