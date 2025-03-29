package com.remitly.axan18.swift_api.mappers;

import com.remitly.axan18.swift_api.entities.Bank;
import com.remitly.axan18.swift_api.models.BankDTO;
import org.mapstruct.Mapper;

@Mapper
public interface BankMapper {
    BankDTO toBankDTO(Bank bank);
    Bank toBank(BankDTO bankDTO);
}
