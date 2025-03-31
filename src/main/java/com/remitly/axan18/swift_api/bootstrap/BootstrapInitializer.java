package com.remitly.axan18.swift_api.bootstrap;
import com.remitly.axan18.swift_api.exceptions.DataLoadingException;
import com.remitly.axan18.swift_api.repositories.BankRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!integrationTest")
@RequiredArgsConstructor
public class BootstrapInitializer implements CommandLineRunner {
    private final BootstrapData bootstrapData;
    private final BankRepository bankRepository;
    private static final Logger log = LoggerFactory.getLogger(BootstrapData.class);

    @Override
    public void run(String... args) {
        if(bankRepository.count() == 0){
            try {
                bootstrapData.loadBanksData("data/Interns_2025_SWIFT_CODES.xlsx");
            } catch (DataLoadingException e) {
                log.error("Error loading banks data", e);
            }
        }
    }
}
