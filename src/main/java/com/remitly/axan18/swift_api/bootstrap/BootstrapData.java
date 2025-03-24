package com.remitly.axan18.swift_api.bootstrap;

import com.remitly.axan18.swift_api.entities.Bank;
import com.remitly.axan18.swift_api.repositories.BankRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {
    private final BankRepository bankRepository;
    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadBanksData();
    }

    private void loadBanksData(){
        try (InputStream fileInputStream = new ClassPathResource("data/Interns_2025_SWIFT_CODES.xlsx").getInputStream();
            Workbook workbook = new XSSFWorkbook(fileInputStream)){
            Sheet sheet = workbook.getSheetAt(0);
            Map<String, Integer> columnIndexMap = extractColumnIndexMap(sheet);
            List<Bank> banks = new ArrayList<>();

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                Bank bank = parseRowToBank(row, columnIndexMap);
                banks.add(bank);
            }
            bankRepository.saveAllAndFlush(banks);
        } catch (IOException e) {
            throw new IllegalStateException("Error reading XLSX file from classpath", e);
        }

    }
    private Bank parseRowToBank(Row row, Map<String, Integer> columnIndexMap) {
        return Bank.builder()
                .swift(getCellValue(row, columnIndexMap.get("SWIFT CODE")).trim())
                .name(getCellValue(row, columnIndexMap.get("NAME")).trim())
                .address(getCellValue(row, columnIndexMap.get("ADDRESS")).trim())
                .countryCodeISO2(getCellValue(row, columnIndexMap.get("COUNTRY ISO2 CODE")).trim())
                .build();
    }

    private static String getCellValue(Row row, Integer colIndex) {
        if (colIndex == null) return null;
        Cell cell = row.getCell(colIndex);
        if (cell == null) return null;

        DataFormatter formatter = new DataFormatter();
        String value = formatter.formatCellValue(cell);
        return value.isEmpty() ? null : value.trim();
    }
    private Map<String, Integer> extractColumnIndexMap(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        if (headerRow == null) {
            throw new IllegalStateException("XLSX file has no header row!");
        }

        Map<String, Integer> columnIndexMap = new HashMap<>();
        for (Cell cell : headerRow) {
            columnIndexMap.put(cell.getStringCellValue(), cell.getColumnIndex());
        }
        return columnIndexMap;
    }
}
