package com.remitly.axan18.swift_api.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.remitly.axan18.swift_api.entities.Bank;
import lombok.*;

import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class BankDTO{
    private String address;
    private String bankName;
    private String countryISO2;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String countryName;
    private Boolean isHeadquarter;
    private String swiftCode;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final List<BankDTO> branches;

    public BankDTO(Bank branch) {
        this.swiftCode = branch.getSwiftCode();
        this.countryISO2 = branch.getCountryISO2();
        this.countryName = branch.getCountryName();
        this.bankName = branch.getBankName();
        this.address = branch.getAddress();
        this.isHeadquarter = branch.getIsHeadquarter();
        this.branches = null;
    }

    public BankDTO(Bank headquarter, List<BankDTO> branches) {
        this.swiftCode = headquarter.getSwiftCode();
        this.countryISO2 = headquarter.getCountryISO2();
        this.countryName = headquarter.getCountryName();
        this.bankName = headquarter.getBankName();
        this.address = headquarter.getAddress();
        this.isHeadquarter = headquarter.getIsHeadquarter();
        this.branches = branches;
    }
}
