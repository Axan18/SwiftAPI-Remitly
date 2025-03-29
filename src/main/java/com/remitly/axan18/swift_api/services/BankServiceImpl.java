package com.remitly.axan18.swift_api.services;

import com.remitly.axan18.swift_api.exceptions.CannotDeleteEntityException;
import com.remitly.axan18.swift_api.mappers.BankMapper;
import com.remitly.axan18.swift_api.models.BankDTO;
import com.remitly.axan18.swift_api.models.BankHeadquarterDTO;
import com.remitly.axan18.swift_api.models.CountryWithSwiftCodesDTO;
import com.remitly.axan18.swift_api.repositories.BankRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class BankServiceImpl implements BankService{
    private final BankRepository bankRepository;
    private final BankMapper bankMapper;

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
    public boolean addBank(BankDTO bankDTO) {
        try {
            bankRepository.save(bankMapper.toBank(bankDTO));
            return true;
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateKeyException("Bank with this swiftCode already exists", e);
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
        List<BankDTO> banks = bankRepository.getBanksByCountryCodeISO2(countryCode).stream().map(
                bank -> {
                    return BankDTO.builder()
                            .address(bank.getAddress())
                            .name(bank.getName())
                            .countryCodeISO2(bank.getCountryCodeISO2())
                            .isHeadquarter(bank.getIsHeadquarter())
                            .swift(bank.getSwift())
                            .branches(null)
                            .countryName(null)
                            .build();
                }
        ).toList();
        return CountryWithSwiftCodesDTO.builder()
                .countryISO2(countryCode)
                .countryName(new Locale.Builder().setRegion(countryCode).build().getDisplayCountry().toUpperCase())
                .swiftCodes(banks)
                .build();
    }

    @Override
    public BankDTO getBankBySwift(String swiftCode) {
        return null;
    }
}
