package com.remitly.axan18.swift_api.bootstrap;

import com.remitly.axan18.swift_api.entities.Bank;
import com.remitly.axan18.swift_api.repositories.BankRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(BootstrapTestConfig.class)
class BootstrapDataTest {
    @Autowired
    BankRepository bankRepository;

    @Test
    void testBootStrap(){
        long bankCount = bankRepository.count();
        assertTrue(bankCount > 0, "Bank table should contain at least one record");
        List<Bank> banks = bankRepository.findAll();
        assertFalse(banks.isEmpty(), "Bank list should not be empty");
        Bank bank = banks.getFirst();
        assertAll("Bank entity integrity",
                () -> assertNotNull(bank.getSwift(), "Swift code should not be null"),
                () -> assertEquals(11, bank.getSwift().length(), "Swift code should be 11 characters"),
                () -> assertNotNull(bank.getCountryCodeISO2(), "Country code should not be null"),
                () -> assertEquals(2, bank.getCountryCodeISO2().length(), "Country ISO2 code should be 2 characters"),
                () -> assertNotNull(bank.getName(), "Bank name should not be null"),
                () -> assertFalse(bank.getName().isBlank(), "Bank name should not be empty")
        );
    }

}