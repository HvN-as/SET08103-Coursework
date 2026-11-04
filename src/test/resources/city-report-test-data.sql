CREATE DATABASE IF NOT EXISTS city_report_test;

USE city_report_test;

CREATE TABLE IF NOT EXISTS country (
                                       Code CHAR(3) PRIMARY KEY,
    Name VARCHAR(52) NOT NULL,
    Continent VARCHAR(30) NOT NULL,
    Region VARCHAR(30) NOT NULL
    );

CREATE TABLE IF NOT EXISTS city (
                                    ID INT PRIMARY KEY AUTO_INCREMENT,
                                    Name VARCHAR(35) NOT NULL,
    CountryCode CHAR(3) NOT NULL,
    District VARCHAR(30) NOT NULL,
    Population INT NOT NULL,
    FOREIGN KEY (CountryCode) REFERENCES country(Code)
    );
INSERT INTO country (Code, Name, Continent, Region)
VALUES
    ('JPN', 'Japan', 'Asia', 'Eastern Asia'),
    ('IND', 'India', 'Asia', 'Southern Asia'),
    ('GBR', 'United Kingdom', 'Europe', 'British Islands'),
    ('USA', 'United States', 'North America', 'North America');
INSERT INTO city (Name, CountryCode, District, Population)
VALUES
    ('Tokyo', 'JPN', 'Tokyo', 9000000),
    ('Osaka', 'JPN', 'Osaka', 2700000),
    ('Mumbai', 'IND', 'Maharashtra', 12000000),
    ('Delhi', 'IND', 'Delhi', 11000000),
    ('London', 'GBR', 'England', 8000000),
    ('Manchester', 'GBR', 'England', 500000),
    ('New York', 'USA', 'New York', 8500000),
    ('Los Angeles', 'USA', 'California', 4000000);
