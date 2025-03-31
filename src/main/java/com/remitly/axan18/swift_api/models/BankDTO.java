package com.remitly.axan18.swift_api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
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
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String countryName;
    private String name;
    private String address;
    private Boolean isHeadquarter;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<BankDTO> branches;

    public BankDTO(Bank branch) {
        this.swift = branch.getSwift();
        this.countryCodeISO2 = branch.getCountryCodeISO2();
        this.countryName = branch.getCountryName();
        this.name = branch.getName();
        this.address = branch.getAddress();
        this.isHeadquarter = branch.getIsHeadquarter();
        this.branches = null;
    }

    public BankDTO(Bank headquarter, List<BankDTO> branches) {
        this.swift = headquarter.getSwift();
        this.countryCodeISO2 = headquarter.getCountryCodeISO2();
        this.countryName = headquarter.getCountryName();
        this.name = headquarter.getName();
        this.address = headquarter.getAddress();
        this.isHeadquarter = headquarter.getIsHeadquarter();
        this.branches = branches;
    }
}
