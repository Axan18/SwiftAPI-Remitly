package com.remitly.axan18.swift_api.repositories;

import com.remitly.axan18.swift_api.entities.Bank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BankRepository extends JpaRepository<UUID, Bank> {

}
