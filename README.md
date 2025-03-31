# SwiftAPI-Remitly
#### Remitly 2025 Recruitment Task
Application parses a XSLX file and stores the data in a PostgreSQL database.
With a help of a REST API, the data can be retrieved, updated and deleted.
To allow efficient rerieval of the data, indexes for column "countryiso2" 
and "swiftcode" (auto-generated for PK by Postgres) are created.
## Technologies
- Java 22
  - SpringBoot
  - Hibernate
  - Maven
  - Lombok
  - Apache POI
  - JUnit
  - Mockito
- PostgreSQL
- Docker & Docker Compose

## Setup and Run
1. Clone the repository
```bash
git clone https://github.com/Axan18/SwiftAPI-Remitly.git
```
2. Build the project
```bash
mvn clean install
```
3. Run the project
```bash
docker-compose up --build -d

4. Access the API
```bash
 http://localhost:8080
```
If you would like to run tests, you can do so by running:
> mvn test
## Parser
The parser reads the data from the XLSX file and stores it in the database.
As many records in provided XLSX file do not have address details, the parser will accept them anyway.
In GET requests, the SWIFT codes and country codes are case-sensitive.

To parse other XLSX files, you can replace the file in the resources folder,
change file name in 
>src/main/java/com/remitly/axan18/swift_api/bootstrap/BootstrapInitializer.java

class and restart the application.

## API Endpoints
- GET: /v1/swift-codes/{swift-code}
  - Retrieve details of a single SWIFT code whether for a headquarters or branches.
- GET:  /v1/swift-codes/country/{countryISO2code}
  - Return all SWIFT codes with details for a specific country (both headquarters and branches).
- POST:  /v1/swift-codes:
  - Adds new SWIFT code entries to the database for a specific country.
- DELETE:  /v1/swift-codes/{swift-code}
  - Deletes swift-code data if swiftCode matches the one in the database.

