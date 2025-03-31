package com.remitly.axan18.swift_api.bootstrap;

import com.remitly.axan18.swift_api.repositories.BankRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
@TestConfiguration
public class BootstrapTestConfig {
    @Bean
    public BootstrapData bootstrapData(BankRepository bankRepository, BankValidator validator){
        return new BootstrapData(bankRepository, validator);
    }
    @Bean
    public BankValidator bankValidator() {
        return new BankValidator();
    }

}
