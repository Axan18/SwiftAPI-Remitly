package com.remitly.axan18.swift_api.services;

import com.remitly.axan18.swift_api.entities.Bank;
import com.remitly.axan18.swift_api.mappers.BankMapper;
import com.remitly.axan18.swift_api.models.BankDTO;
import com.remitly.axan18.swift_api.models.CountryWithSwiftCodesDTO;
import com.remitly.axan18.swift_api.models.NewBankDTO;
import com.remitly.axan18.swift_api.repositories.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
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
                .countryISO2("AL")
                .swiftCode(testSwift)
                .bankName("UNITED BANK OF ALBANIA SH.A")
                .address("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023")
                .countryName("ALBANIA")
                .isHeadquarter(true)
                .build();
    }
    @Test
    void testDeleteBank(){
        when(bankRepository.existsById(testSwift)).thenReturn(true);
        doNothing().when(bankRepository).deleteById(testSwift);
        boolean result = bankService.deleteBySwift(testSwift);
        assertTrue(result);
        verify(bankRepository).deleteById(testSwift);
    }
    @Test
    void testAddBank(){
        NewBankDTO bankDTO = NewBankDTO.builder()
                .countryISO2("AL")
                .swiftCode(testSwift)
                .bankName("UNITED BANK OF ALBANIA SH.A")
                .address("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023")
                .countryName("ALBANIA")
                .build();
        when(bankRepository.save(any(Bank.class))).thenReturn(bank);
        when(bankMapper.toBank(bankDTO)).thenReturn(bank);
        boolean result = bankService.addBank(bankDTO);
        assertTrue(result);
        verify(bankRepository).save(any(Bank.class));
    }
    @Test
    void getBanksByCountryCode() {
        String countryCode = "PL";
        Bank bank1 = Bank.builder()
                .countryName("POLAND")
                .countryISO2("PL")
                .isHeadquarter(false)
                .swiftCode("ABCDEFGEHQQ")
                .bankName("Bank1")
                .address("Address1")
                .build();
        Bank bank2 = Bank.builder()
                .countryName("POLAND")
                .countryISO2("PL")
                .isHeadquarter(false)
                .swiftCode("ABCDEFGEHQW")
                .bankName("Bank2")
                .address("Address2")
                .build();
        Bank heq = Bank.builder()
                .countryName("POLAND")
                .countryISO2("PL")
                .isHeadquarter(false)
                .swiftCode("ABCDEFGEXXX")
                .bankName("BankHeadquarter")
                .address("AddressHq")
                .build();
        when(bankRepository.getBanksByCountryISO2(countryCode)).thenReturn(List.of(bank1, bank2, heq));
        CountryWithSwiftCodesDTO result = bankService.getBanksByCountryCode(countryCode);
        assertNotNull(result);
        assertEquals(countryCode, result.getCountryISO2());
        assertEquals(new Locale.Builder().setRegion(countryCode).build().getDisplayCountry().toUpperCase(), result.getCountryName());
        assertEquals(3, result.getSwiftCodes().size());
        assertEquals("ABCDEFGEXXX", result.getSwiftCodes().get(2).getSwiftCode());
        verify(bankRepository, times(1)).getBanksByCountryISO2(countryCode);
    }

    @Test
    void getSingleBankBySwift() {
        when(bankRepository.getBanksBySwift(testSwift.substring(0, 8) + "%")).thenReturn(List.of(bank));
        BankDTO result = bankService.getBankBySwift(testSwift);
        assertNotNull(result);
        assertEquals(testSwift, result.getSwiftCode());
        assertEquals("AL", result.getCountryISO2());
        assertEquals("UNITED BANK OF ALBANIA SH.A", result.getBankName());
        assertEquals("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023", result.getAddress());
        assertEquals("ALBANIA", result.getCountryName());
        verify(bankRepository, times(1)).getBanksBySwift(testSwift.substring(0, 8) + "%");
    }
    @Test
    void getHeadquarterBankBySwift(){
        String testSwift = "AAISALTRXXX";
        Bank headquarter = bank;
        Bank branch1 = Bank.builder()
                .countryISO2("AL")
                .swiftCode("AAISALTR123")
                .bankName("UNITED BANK OF ALBANIA SH.A")
                .address("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023")
                .countryName("ALBANIA")
                .isHeadquarter(false)
                .build();
        when(bankRepository.getBanksBySwift(testSwift.substring(0, 8) + "%")).thenReturn(List.of(headquarter,branch1));
        BankDTO result = bankService.getBankBySwift(testSwift);
        assertNotNull(result);
        assertEquals(testSwift, result.getSwiftCode());
        assertEquals("AL", result.getCountryISO2());
        assertEquals("UNITED BANK OF ALBANIA SH.A", result.getBankName());
        assertEquals("HYRJA 3 RR. DRITAN HOXHA ND. 11 TIRANA, TIRANA, 1023", result.getAddress());
        assertEquals("ALBANIA", result.getCountryName());
        assertEquals(1, result.getBranches().size());
        verify(bankRepository, times(1)).getBanksBySwift("AAISALTR%");
    }
}
