package com.remitly.axan18.swift_api.mappers;

import com.remitly.axan18.swift_api.entities.Bank;
import com.remitly.axan18.swift_api.models.BankDTO;
import com.remitly.axan18.swift_api.models.NewBankDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BankMapper {
    Bank toBank(BankDTO bankDTO);
    Bank toBank(NewBankDTO bankDTO);
}
