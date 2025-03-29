package com.remitly.axan18.swift_api.services;

import com.remitly.axan18.swift_api.entities.Bank;
import com.remitly.axan18.swift_api.mappers.BankMapper;
import com.remitly.axan18.swift_api.models.BankDTO;
import com.remitly.axan18.swift_api.repositories.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class BankServiceTest {

    @Mock
    private BankRepository bankRepository;

    @Mock
    private BankMapper bankMapper;

    @InjectMocks
    BankServiceImpl bankService;

    Bank bank;
    String testSwift = "AAISALTRXXX";

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);

        bank = Bank.builder()
                .countryCodeISO2("AL")
                .swift(testSwift)
                .name("UNITED BANK OF ALBANIA SH.A")
                .address("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023")
                .countryName("ALBANIA")
                .build();
    }
    @Test
    void testDeleteBank(){
        doNothing().when(bankRepository).deleteById(testSwift);
        boolean result = bankService.deleteBySwift(testSwift);
        assertTrue(result);
        verify(bankRepository).deleteById(testSwift);
    }
    @Test
    void testAddBank(){
        BankDTO bankDTO = BankDTO.builder()
                .countryCode("AL")
                .swift(testSwift)
                .name("UNITED BANK OF ALBANIA SH.A")
                .address("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023")
                .countryName("ALBANIA")
                .build();
        when(bankRepository.save(any(Bank.class))).thenReturn(bank);
        when(bankMapper.toBank(bankDTO)).thenReturn(bank);
        boolean result = bankService.addBank(bankDTO);
        assertTrue(result);
        verify(bankRepository).save(any(Bank.class));
    }

}
