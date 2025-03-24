package com.remitly.axan18.swift_api.bootstrap;

import com.remitly.axan18.swift_api.repositories.BankRepository;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class BootstrapTestConfig {
    @Bean
    public BootstrapData bootstrapData(BankRepository bankRepository){
        return new BootstrapData(bankRepository);
    }
}
