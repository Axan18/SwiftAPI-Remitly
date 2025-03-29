package com.remitly.axan18.swift_api.bootstrap;

import com.remitly.axan18.swift_api.entities.Bank;
import com.remitly.axan18.swift_api.exceptions.DataLoadingException;
import com.remitly.axan18.swift_api.repositories.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({BootstrapData.class, BootstrapTestConfig.class})
class BootstrapDataTest {
    @Autowired
    BankRepository bankRepository;
    @Autowired
    private BootstrapData bootstrapData;

    @BeforeEach
    void setUp(){
        bankRepository.deleteAll();
    }

    @ParameterizedTest
    @ValueSource(strings = "data/testOriginal.xlsx")
    void testBootStrap(String xlsxPath){
        try {
            bootstrapData.loadBanksData(xlsxPath);
        } catch (DataLoadingException e) {
            fail();
        }
        long bankCount = bankRepository.count();
        assertTrue(bankCount > 0, "Bank table should contain at least one record");
        List<Bank> banks = bankRepository.findAll();
        assertFalse(banks.isEmpty(), "Bank list should not be empty");
        Bank bank = banks.getFirst();
        assertAll("Bank entity integrity",
            () -> assertNotNull(bank.getSwift(), "Swift code should not be null"),
            () -> assertEquals(11, bank.getSwift().length(), "Swift code should be 11 characters"),
            () -> assertNotNull(bank.getCountryCodeISO2(), "Country code should not be null"),
            () -> assertEquals(2, bank.getCountryCodeISO2().length(), "Country ISO2 code should be 2 characters"),
            () -> assertNotNull(bank.getName(), "Bank name should not be null"),
            () -> assertFalse(bank.getName().isBlank(), "Bank name should not be empty"),
            () -> assertFalse(bank.getCountryName().isBlank(), "Country name should not be empty"),
            () -> assertNotNull(bank.getIsHeadquarter(), "Bank should be headquarter or a branch")
        );
    }

    /**
     * Test checks parsing ISO2 country codes during loading records from file.
     * File contains 10 records: 4-correct, 4-incorrect, 2-soon correct
     * <p>
     * Incorrect ones are:
     * <p>"Ala" - too long</p>
     * <p>"11" - numbers are not valid country codes</p>
     * <p>"B" - too short</p>
     * <p>null - not allowed</p>
     * </p>
     * <p>
     *      Besides that 2 records need adjustment
     *      <p>"bg" to "BG"</p>
     *      <p>" PL" to "PL</p>
     * </p>
     * @param xlsxPath path to .xlsx test file
     */
    @ParameterizedTest
    @ValueSource(strings = "data/testIncorrectCountryCode.xlsx")
    void testCountryCodeParsing(String xlsxPath){
        try {
            bootstrapData.loadBanksData(xlsxPath);
        } catch (DataLoadingException e) {
            fail();
        }
        assertEquals(6,bankRepository.count());
    }
    /**
     * Test checks parsing Swift codes during loading records from file.
     * File contains 10 records: 5-correct, 4-incorrect, 1-soon correct
     * <p>
     * Incorrect ones are:
     * <p>"AAISALTRXXXa" - too long</p>
     * <p>"ABIEBGS1XXX2" - too long</p>
     * <p>"ADCRBGS1XX" - too short</p>
     * <p>"AFAAUY`1XXX" - not allowed character</p>
     * </p>
     * <p>
     *      Besides that 1 record need adjustment
     *      <p>"AGRaMCM1XXX" to "AGRAMCM1XXX"</p>
     * </p>
     * @param xlsxPath path to .xlsx test file
     */
    @ParameterizedTest
    @ValueSource(strings = "data/testIncorrectSwift.xlsx")
    void testIncorrectSwiftCodes(String xlsxPath){
        try {
            bootstrapData.loadBanksData(xlsxPath);
        } catch (DataLoadingException e) {
            fail();
        }
        assertEquals(6,bankRepository.count());
    }

    /**
     * Tests empty xlsx file.
     * @param xlsxPath
     */
    @ParameterizedTest
    @ValueSource(strings = "data/testEmptyFile.xlsx")
    void testEmptyFile(String xlsxPath){
        assertThrows(DataLoadingException.class,() -> bootstrapData.loadBanksData(xlsxPath));
    }

    /**
     * Tests file without swift code column.
     * @param xlsxPath
     */
    @ParameterizedTest
    @ValueSource(strings = "data/testInvalidColumns.xlsx")
    void testInvalidColumns(String xlsxPath){
        assertThrows(DataLoadingException.class,() -> bootstrapData.loadBanksData(xlsxPath));
    }

    static Stream<File> testFilesProvider() {
        File folder = new File("src/test/resources/test-data/");
        return Arrays.stream(folder.listFiles((dir, name) -> name.endsWith(".xlsx")));
    }
}