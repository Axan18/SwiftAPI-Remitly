package com.remitly.axan18.swift_api.services;

import com.remitly.axan18.swift_api.entities.Bank;
import com.remitly.axan18.swift_api.models.BankDTO;
import com.remitly.axan18.swift_api.models.BankHeadquarterDTO;

import java.util.List;

public interface BankService {
    String deleteBySwift(String swiftCode);
    List<BankDTO> getBanksByCountryCode(String countryCode);
    BankHeadquarterDTO getBankHeadquarterBySwift(String swiftCode);
    BankDTO getBankBySwift(String swiftCode);
}
