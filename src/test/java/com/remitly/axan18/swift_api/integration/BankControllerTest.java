package com.remitly.axan18.swift_api.integration;

import com.remitly.axan18.swift_api.entities.Bank;
import com.remitly.axan18.swift_api.models.NewBankDTO;
import com.remitly.axan18.swift_api.repositories.BankRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("integrationTest")
@Testcontainers
public class BankControllerTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withReuse(true);

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BankRepository bankRepository;

    @Autowired
    private MockMvc mockMvc;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }
    @BeforeEach
    void setup() {
        bankRepository.deleteAll();
    }
    @Test
    void shouldAddBank() {
        NewBankDTO request = new NewBankDTO("", "Test", "PL", "POLAND", false, "TESTPLSWIFT");

        ResponseEntity<Void> response = restTemplate.postForEntity("/v1/swift-codes", request, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(bankRepository.existsById("TESTPLSWIFT")).isTrue();
    }

    @Test
    void shouldNotAddDuplicateBank() {
        Bank bank = new Bank("TESTPLSWIFT", "PL", "Test", "", "Poland", true);
        bankRepository.save(bank);
        NewBankDTO duplicateRequest = new NewBankDTO("", "Test", "PL", "POLAND", false, "TESTPLSWIFT");
        ResponseEntity<String> response = restTemplate.postForEntity("/v1/swift-codes", duplicateRequest, String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    }
    @Test
    void shouldRetrieveBankBySwiftCode() throws Exception {
        Bank bank = new Bank("TESTXX33XXX", "US", "name", "", "United States", true);
        bankRepository.save(bank);
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/swift-codes/TESTXX33XXX"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.swiftCode").value("TESTXX33XXX"))
                .andExpect(jsonPath("$.bankName").value("name"));
    }

    @Test
    void shouldDeleteBank() throws Exception {
        bankRepository.save(new Bank("TESTXX33XXX", "US", "name", "", "United States", true));
        mockMvc.perform(delete("/v1/swift-codes/TESTXX33XXX"))
                .andExpect(status().isOk());
        Assertions.assertFalse(bankRepository.existsById("TESTXX33XXX"));
    }
    @Test
    void shouldNotDeleteBank() throws Exception {
        bankRepository.save(new Bank("TESTXX33XXX", "US", "name", "", "United States", true));
        mockMvc.perform(delete("/v1/swift-codes/TESTXX33XXX"))
                .andExpect(status().isOk());
        Assertions.assertFalse(bankRepository.existsById("TESTXX33XXX"));
    }
    @Test
    void shouldGetBanksByCountryCode() throws Exception {
        bankRepository.save(new Bank("TESTXX33XXX", "US", "name", "", "United States", true));
        bankRepository.save(new Bank("TESTXX44XXX", "US", "name", "", "United States", false));
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/swift-codes/country/US"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countryISO2").value("US"))
                .andExpect(jsonPath("$.swiftCodes.length()").value(2));
    }
    @Test
    void shouldNotGetBankBySwift() throws Exception {
        Bank bank = new Bank("TESTXX33XXX", "US", "name", "", "United States", true);
        bankRepository.save(bank);

        mockMvc.perform(MockMvcRequestBuilders.get("/v1/swift-codes/TESTUs33XXX"))
                .andExpect(status().isBadRequest());
    }

}