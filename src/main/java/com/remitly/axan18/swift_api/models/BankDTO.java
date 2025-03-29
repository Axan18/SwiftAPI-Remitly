package com.remitly.axan18.swift_api.models;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankDTO {
    private String swift;
    private String countryCode;
    private String countryName;
    private String name;
    private String address;
    private Boolean isHeadquarter;
}
