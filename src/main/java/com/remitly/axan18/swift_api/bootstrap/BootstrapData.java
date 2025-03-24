package com.remitly.axan18.swift_api.bootstrap;

import com.remitly.axan18.swift_api.repositories.BankRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BankRepository bankRepository;

    @Override
    public void run(String... args) throws Exception {

    }

    private void loadBanksData(){

    }
}
