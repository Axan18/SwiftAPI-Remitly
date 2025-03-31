package com.remitly.axan18.swift_api.bootstrap;

import com.remitly.axan18.swift_api.entities.Bank;
import com.remitly.axan18.swift_api.exceptions.DataLoadingException;
import com.remitly.axan18.swift_api.exceptions.InvalidCountryCodeException;
import com.remitly.axan18.swift_api.exceptions.InvalidSwiftCodeException;
import com.remitly.axan18.swift_api.repositories.BankRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.EmptyFileException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BootstrapData{
    private final BankRepository bankRepository;
    private final BankValidator validator;
    private static final Logger log = LoggerFactory.getLogger(BootstrapData.class);

    public void loadBanksData(String xlsxPath) throws DataLoadingException {
        try (InputStream fileInputStream = new ClassPathResource(xlsxPath).getInputStream();
            Workbook workbook = new XSSFWorkbook(fileInputStream)){
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Integer> columnIndexMap = extractColumnIndexMap(sheet);
            List<Bank> banks = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                try{
                    banks.add(parseRowToBank(row, columnIndexMap));

                }catch (InvalidSwiftCodeException e){
                    log.info("Invalid SWIFT code at row: {}", row.getRowNum(), e);
                }catch (IllegalStateException e){
                    log.info("Invalid data in row: {}", row.getRowNum(), e);
                }catch (InvalidCountryCodeException e){
                    log.info("Invalid country code", e);
                }
            }
            bankRepository.saveAllAndFlush(banks);
        } catch (IOException | EmptyFileException e) {
            throw new DataLoadingException("Error reading XLSX file from classpath", e);
        }
    }

    private Bank parseRowToBank(Row row, Map<String, Integer> columnIndexMap) throws InvalidSwiftCodeException, InvalidCountryCodeException {
        String swiftCode = getCellValue(row, columnIndexMap.get("SWIFT CODE"));
        String name = getCellValue(row, columnIndexMap.get("NAME"));
        String address = getCellValue(row, columnIndexMap.get("ADDRESS"));
        String countryCodeISO2 = getCellValue(row, columnIndexMap.get("COUNTRY ISO2 CODE"));
        String countryName = getCellValue(row, columnIndexMap.get("COUNTRY NAME"));

        if(swiftCode==null || name == null || countryCodeISO2 == null || countryName == null)
            throw new IllegalStateException(String.format(
                    "Missing required fields: SWIFT Code: %s, Name: %s, Country Code: %s, Country name: %s",
                    swiftCode, name, countryCodeISO2, countryName
            ));
        swiftCode = swiftCode.toUpperCase();
        countryCodeISO2 = countryCodeISO2.toUpperCase();
        countryName = countryName.toUpperCase();

        if(!validator.isValidSwift(swiftCode))
            throw new InvalidSwiftCodeException("SWIFT code contains characters that are not number or letters");
        if(!validator.isValidISO2(countryCodeISO2, countryName))
            throw new InvalidCountryCodeException("Country Code must be valid iso2 country code");


        return Bank.builder()
                    .swiftCode(swiftCode)
                    .bankName(name)
                    .address(address)
                    .countryISO2(countryCodeISO2)
                    .isHeadquarter(isHeadquarter(swiftCode))
                    .countryName(countryName)
                    .build();
    }

    private static String getCellValue(Row row, Integer colIndex) {
        if (colIndex == null) return null;
        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;

        DataFormatter formatter = new DataFormatter();
        String value = formatter.formatCellValue(cell).trim();
        if(value.isEmpty()){
            return null;
        }
        return value;
    }

    private Map<String, Integer> extractColumnIndexMap(Sheet sheet) throws DataLoadingException{
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalStateException("XLSX file has no header row!");
        }

        Map<String, Integer> columnIndexMap = new HashMap<>();
        for (Cell cell : headerRow) {
            columnIndexMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }
        columnIndexMap.remove("");// removing empty column
        if(!validator.areValidColumns(columnIndexMap.keySet()))
            throw new DataLoadingException("Invalid columns in datafile");
        return columnIndexMap;
    }

    private static Boolean isHeadquarter(String swiftCode){
        return swiftCode.startsWith("XXX", 8);//last 3 characters are XXX in headquarter
    }
}
