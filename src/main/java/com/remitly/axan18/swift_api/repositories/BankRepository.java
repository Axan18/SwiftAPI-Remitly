package com.remitly.axan18.swift_api.repositories;

import com.remitly.axan18.swift_api.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankRepository extends JpaRepository<Bank, String> {
    List<Bank> getBanksByCountryCodeISO2(String countryCode);
}
