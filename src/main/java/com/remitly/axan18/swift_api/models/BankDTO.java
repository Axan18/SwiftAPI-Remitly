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
    private String address;
    private String bankName;
    private String countryISO2;
    private Boolean isHeadquarter;
    private String swiftCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String countryName;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<BankDTO> branches;

    public BankDTO(Bank branch) {
        this.swiftCode = branch.getSwift();
        this.countryISO2 = branch.getCountryCodeISO2();
        this.countryName = branch.getCountryName();
        this.bankName = branch.getName();
        this.address = branch.getAddress();
        this.isHeadquarter = branch.getIsHeadquarter();
        this.branches = null;
    }

    public BankDTO(Bank headquarter, List<BankDTO> branches) {
        this.swiftCode = headquarter.getSwift();
        this.countryISO2 = headquarter.getCountryCodeISO2();
        this.countryName = headquarter.getCountryName();
        this.bankName = headquarter.getName();
        this.address = headquarter.getAddress();
        this.isHeadquarter = headquarter.getIsHeadquarter();
        this.branches = branches;
    }
}
