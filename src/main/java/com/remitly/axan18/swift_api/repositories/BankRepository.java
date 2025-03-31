package com.remitly.axan18.swift_api.repositories;

import com.remitly.axan18.swift_api.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends JpaRepository<Bank, String> {
    List<Bank> getBanksByCountryISO2(String countryCode);
    @Query("SELECT b from Bank b where b.swiftCode LIKE :swift")
    List<Bank> getBanksBySwift(@Param("swift") String swift);
    Optional<Bank> getBankBySwiftCode(String swift);
}
