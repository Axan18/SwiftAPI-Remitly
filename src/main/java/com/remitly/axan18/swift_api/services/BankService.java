package com.remitly.axan18.swift_api.services;

import com.remitly.axan18.swift_api.models.BankDTO;
import com.remitly.axan18.swift_api.models.CountryWithSwiftCodesDTO;
import com.remitly.axan18.swift_api.models.NewBankDTO;

public interface BankService {
    boolean deleteBySwift(String swiftCode);
    boolean addBank(NewBankDTO bankDTO);
    CountryWithSwiftCodesDTO getBanksByCountryCode(String countryCode);
    BankDTO getBankBySwift(String swiftCode);
}
