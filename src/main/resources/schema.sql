CREATE TABLE bank (
    swiftCode VARCHAR(11) PRIMARY KEY,
    countryISO2 VARCHAR(2) NOT NULL,
    bankName VARCHAR(255) NOT NULL,
    address VARCHAR(255),
    countryName VARCHAR(255) NOT NULL,
    isHeadquarter BOOLEAN NOT NULL
);

-- Create index on country_code_iso2
CREATE INDEX idx_country_iso2 ON bank (countryiso2);
