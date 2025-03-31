package com.remitly.axan18.swift_api.controllers;

import com.remitly.axan18.swift_api.models.BankDTO;
import com.remitly.axan18.swift_api.models.CountryWithSwiftCodesDTO;
import com.remitly.axan18.swift_api.services.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class BankController {
    public static final String SWIFT_PATH = "/v1/swift-codes";
    public static final String SWIFT_PATH_COUNTRY = SWIFT_PATH + "/country";

    private final BankService bankService;

    @PostMapping(SWIFT_PATH_COUNTRY)
    public ResponseEntity<String> addBank(@RequestBody BankDTO bankDTO) {
        if (bankService.addBank(bankDTO)) {
            return new ResponseEntity<>("Bank added successfully", HttpStatus.CREATED);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error, bank has not been added");
    }

    @DeleteMapping(SWIFT_PATH + "/{swiftCode}")
    public ResponseEntity<String> deleteBank(@PathVariable String swiftCode) {
        if (bankService.deleteBySwift(swiftCode)) {
            return new ResponseEntity<>("Bank deleted successfully", HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error, bank has not been deleted");
    }

    @GetMapping(SWIFT_PATH + "/{swiftCode}")
    public ResponseEntity<BankDTO> getBankBySwift(@PathVariable String swiftCode) {
        BankDTO bankDTO = bankService.getBankBySwift(swiftCode);
        if (bankDTO != null) {
            return new ResponseEntity<>(bankDTO, HttpStatus.OK);
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Bank not found");
    }

    @GetMapping(SWIFT_PATH_COUNTRY + "/{countryISO2code}")
    public ResponseEntity<CountryWithSwiftCodesDTO> getBanksByCountryCode(@PathVariable String countryISO2code) {
        return new ResponseEntity<>(bankService.getBanksByCountryCode(countryISO2code), HttpStatus.OK);
    }


}
