package com.remitly.axan18.swift_api.services;

import com.remitly.axan18.swift_api.models.BankDTO;
import com.remitly.axan18.swift_api.models.BankHeadquarterDTO;
import com.remitly.axan18.swift_api.repositories.BankRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BankServiceImpl implements BankService{
    private final BankRepository bankRepository;

    @Override
    public String deleteBySwift(String swiftCode){
        try{
            bankRepository.deleteById(swiftCode);
            return "Bank deleted successfully";
        }catch (EmptyResultDataAccessException e) {
            return "Bank with swift " + swiftCode + " does not exist.";
        } catch (IllegalArgumentException e) {
            return "Provided swift is null.";
        } catch (DataAccessException e) {
            return "Database error occurred while deleting bank: " + e.getMessage();
        }
    }



    @Override
    public List<BankDTO> getBanksByCountryCode(String countryCode) {
        return List.of();
    }

    @Override
    public BankHeadquarterDTO getBankHeadquarterBySwift(String swiftCode) {
        return null;
    }

    @Override
    public BankDTO getBankBySwift(String swiftCode) {
        return null;
    }
}
