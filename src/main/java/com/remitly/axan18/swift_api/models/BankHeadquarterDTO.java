package com.remitly.axan18.swift_api.models;

import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankHeadquarterDTO {
    private String swift;
    private String name;
    private String address;
    private String countryCode;
    private String countryName;
    private Boolean isHeadquarter;
    private List<BankBranchDTO> branches;
}
