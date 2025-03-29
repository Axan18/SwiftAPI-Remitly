package com.remitly.axan18.swift_api.models;

import com.remitly.axan18.swift_api.entities.Bank;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class BankDTO {
    private String swift;
    private String countryCodeISO2;
    private String countryName;
    private String name;
    private String address;
    private Boolean isHeadquarter;
    private final List<BankDTO> branches;

}
