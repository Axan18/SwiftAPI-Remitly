package com.remitly.axan18.swift_api.services;

import com.remitly.axan18.swift_api.bootstrap.BankValidator;
import com.remitly.axan18.swift_api.entities.Bank;
import com.remitly.axan18.swift_api.exceptions.CannotDeleteEntityException;
import com.remitly.axan18.swift_api.exceptions.InvalidSwiftCodeException;
import com.remitly.axan18.swift_api.mappers.BankMapper;
import com.remitly.axan18.swift_api.models.BankDTO;
import com.remitly.axan18.swift_api.models.CountryWithSwiftCodesDTO;
import com.remitly.axan18.swift_api.models.NewBankDTO;
import com.remitly.axan18.swift_api.repositories.BankRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.IllformedLocaleException;
import java.util.List;
import java.util.Locale;

@Service
@AllArgsConstructor
public class BankServiceImpl implements BankService{
    private final BankRepository bankRepository;
    private final BankMapper bankMapper;
    private final BankValidator bankValidator;

    @Override
    public boolean deleteBySwift(String swiftCode) {
        if (swiftCode == null)
            throw new IllegalArgumentException("Provided swift is null.");

        if (!bankRepository.existsById(swiftCode))
            throw new EntityNotFoundException("Bank with swift " + swiftCode + " does not exist.");

        try {
            bankRepository.deleteById(swiftCode);
            return true;
        } catch (DataAccessException e) {
            throw new CannotDeleteEntityException("Database error occurred while deleting bank", e);
        }
    }

    @Override
    public boolean addBank(NewBankDTO bankDTO) {
        if (bankDTO == null || bankDTO.getCountryISO2() == null || bankDTO.getBankName() == null ||
                bankDTO.getCountryName() == null || bankDTO.getSwiftCode() == null) {
            throw new IllegalStateException(String.format(
                    "Missing required fields: SWIFT Code: %s, Name: %s, Country Code: %s, Country Name: %s",
                    bankDTO != null ? bankDTO.getSwiftCode() : "null",
                    bankDTO != null ? bankDTO.getBankName() : "null",
                    bankDTO != null ? bankDTO.getCountryISO2() : "null",
                    bankDTO != null ? bankDTO.getCountryName() : "null"
            ));
        }
        Bank bank = bankMapper.toBank(bankDTO);
        if (!bankValidator.isValidISO2(bank.getCountryISO2(), bank.getCountryName()))
            throw new ValidationException("Invalid country code or country name");
        if (!bankValidator.isValidSwift(bank.getSwiftCode()))
            throw new ValidationException("Invalid SWIFT code format");
        if (!bankValidator.isValidHeadquarter(bank.getSwiftCode(), bank.getIsHeadquarter()))
            throw new ValidationException("Invalid headquarter information");
        if (bankRepository.existsById(bank.getSwiftCode())) {
            throw new DuplicateKeyException("Bank with this SWIFT code already exists");
        }

        try {
            bankRepository.save(bank);
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Bank with this SWIFT code already exists", e);
        } catch (ConstraintViolationException e) {
            throw new ValidationException("Invalid bank data: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Bank data cannot be null", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while adding bank", e);
        }
    }

    @Override
    public CountryWithSwiftCodesDTO getBanksByCountryCode(String countryCode) {
        if(countryCode == null || countryCode.length() != 2 || !countryCode.equals(countryCode.toUpperCase())){
            throw new IllformedLocaleException(countryCode);
        }
        List<BankDTO> banks = bankRepository.getBanksByCountryISO2(countryCode).stream().map(
                bank -> BankDTO.builder()
                        .address(bank.getAddress())
                        .bankName(bank.getBankName())
                        .countryISO2(bank.getCountryISO2())
                        .isHeadquarter(bank.getIsHeadquarter())
                        .swiftCode(bank.getSwiftCode())
                        .branches(null)
                        .countryName(null)
                        .build()
        ).toList();
        Locale loc;
        try{
            loc = new Locale.Builder().setRegion(countryCode).build();
        }catch (IllformedLocaleException e) {
            throw new IllformedLocaleException(countryCode);
        }
        String countryName = loc.getDisplayCountry().toUpperCase();
        if(countryName.isEmpty() || countryName.equals(countryCode))
            throw new IllformedLocaleException(countryCode);

        return CountryWithSwiftCodesDTO.builder()
                .countryISO2(countryCode)
                .countryName(countryName)
                .swiftCodes(banks)
                .build();
    }

    @Override
    public BankDTO getBankBySwift(String swiftCode) {
        if(swiftCode == null || swiftCode.length() != 11 || !swiftCode.equals(swiftCode.toUpperCase())){
            throw new InvalidSwiftCodeException(swiftCode);
        }
        if(swiftCode.startsWith("XXX",8)){
            List<Bank> allBanks = bankRepository.getBanksBySwift(swiftCode.substring(0, 8) + "%");
            Bank headquarter = allBanks.stream()
                    .filter(Bank::getIsHeadquarter)
                    .findFirst()
                    .orElseThrow(() -> new EntityNotFoundException("Bank with swift code " + swiftCode + " not found."));

            List<BankDTO> branches = allBanks.stream()
                    .filter(bank -> !bank.getIsHeadquarter())
                    .map(branch -> BankDTO.builder()
                            .address(branch.getAddress())
                            .bankName(branch.getBankName())
                            .countryISO2(branch.getCountryISO2())
                            .isHeadquarter(branch.getIsHeadquarter())
                            .swiftCode(branch.getSwiftCode())
                            .branches(null)
                            .countryName(null)
                            .build())
                    .toList();

            return new BankDTO(headquarter, branches);
        }else{
            Bank bank = bankRepository.getBankBySwiftCode(swiftCode)
                    .orElseThrow(() -> new EntityNotFoundException("Bank with swift code " + swiftCode + " not found."));
            if (bank == null) {
                throw new EntityNotFoundException("Bank with swift code " + swiftCode + " not found.");
            }
            return new BankDTO(bank);
        }
    }
}
