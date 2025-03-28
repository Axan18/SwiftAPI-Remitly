package com.remitly.axan18.swift_api.bootstrap;
import com.remitly.axan18.swift_api.exceptions.DataLoadingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BootstrapInitializer implements CommandLineRunner {
    private final BootstrapData bootstrapData;
    private static final Logger log = LoggerFactory.getLogger(BootstrapData.class);

    @Override
    public void run(String... args) {
        try {
            bootstrapData.loadBanksData("data/Interns_2025_SWIFT_CODES.xlsx");
        } catch (DataLoadingException e) {
            log.warn("Data has not been loaded", e);
        }
    }
}
