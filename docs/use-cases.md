# Population Reporting System — Use Cases

## UC-01 — Generate Country Population Reports

**Primary Actor:** Organisation User

**Goal:** Generate country population reports for the world, a continent or a region.

### Preconditions
- The population reporting system is running.
- The World database is available.
- Country population data is available.

### Main Flow
1. The user selects the country population report.
2. The system displays the available geographical scopes.
3. The user selects World, Continent or Region.
4. The user chooses whether to display all countries or the Top N countries.
5. If Top N is selected, the user enters the required value for N.
6. The system retrieves the relevant country data from the World database.
7. The system sorts the countries by population from largest to smallest.
8. The system displays the country report.
9. The user views the results.

### Report Information
The report displays:
- Code
- Name
- Continent
- Region
- Population
- Capital

### Alternative Flows
- If the selected continent or region is invalid, the system informs the user and requests another selection.
- If an invalid Top N value is entered, the system requests a valid value.

### Postconditions
- The requested country population report is displayed.
- No database data is modified.

---

## UC-02 — Generate City Population Reports

**Primary Actor:** Organisation User

**Goal:** Generate city population reports for different geographical areas.

### Preconditions
- The population reporting system is running.
- The World database is available.
- City population data is available.

### Main Flow
1. The user selects the city population report.
2. The system displays the available geographical scopes.
3. The user selects World, Continent, Region, Country or District.
4. The user chooses whether to display all cities or the Top N cities.
5. If Top N is selected, the user enters the required value for N.
6. The system retrieves the relevant city data.
7. The system sorts the cities by population from largest to smallest.
8. The system displays the city report.
9. The user views the results.

### Report Information
The report displays:
- Name
- Country
- District
- Population

### Alternative Flows
- If the selected geographical area is invalid, the system requests another selection.
- If an invalid Top N value is entered, the system requests a valid value.

### Postconditions
- The requested city population report is displayed.
- No database data is modified.

---

## UC-03 — Generate Capital City Population Reports

**Primary Actor:** Organisation User

**Goal:** Generate population reports for capital cities.

### Preconditions
- The population reporting system is running.
- The World database is available.
- Capital city and population data is available.

### Main Flow
1. The user selects the capital city population report.
2. The system displays the available geographical scopes.
3. The user selects World, Continent or Region.
4. The user chooses whether to display all capital cities or the Top N capital cities.
5. If Top N is selected, the user enters the required value for N.
6. The system retrieves the relevant capital city data.
7. The system sorts the capital cities by population from largest to smallest.
8. The system displays the capital city report.
9. The user views the results.

### Report Information
The report displays:
- Name
- Country
- Population

### Alternative Flows
- If the selected geographical area is invalid, the system requests another selection.
- If an invalid Top N value is entered, the system requests a valid value.

### Postconditions
- The requested capital city population report is displayed.
- No database data is modified.

---

## UC-04 — Generate Population Breakdown Reports

**Primary Actor:** Organisation User

**Goal:** View how population is distributed between people living in cities and people not living in cities.

### Preconditions
- The population reporting system is running.
- The World database is available.
- Population and city data is available.

### Main Flow
1. The user selects the population breakdown report.
2. The system displays the available geographical levels.
3. The user selects Continent, Region or Country.
4. The system retrieves the relevant population data.
5. The system calculates the total population living in cities.
6. The system calculates the percentage of the population living in cities.
7. The system calculates the population not living in cities.
8. The system calculates the percentage of the population not living in cities.
9. The system displays the population breakdown report.
10. The user views the results.

### Report Information
The report displays:
- Name
- Total population
- Total population living in cities
- Percentage living in cities
- Total population not living in cities
- Percentage not living in cities

### Alternative Flows
- If the selected geographical level is invalid, the system requests another selection.
- If required population data is unavailable, the system informs the user that the report cannot be generated.

### Postconditions
- The population breakdown report is displayed.
- No database data is modified.

---

## UC-05 — Generate Population Total Reports

**Primary Actor:** Organisation User

**Goal:** View population totals at different geographical levels.

### Preconditions
- The population reporting system is running.
- The World database is available.

### Main Flow
1. The user selects the population total report.
2. The system displays the available geographical levels.
3. The user selects World, Continent, Region, Country, District or City.
4. If required, the user selects the specific geographical area.
5. The system retrieves the population data from the World database.
6. 6. The system displays the population total and clearly identifies the geographical level being reported.
7. The user views the result.

### Report Information
The system can display the population of:
- World
- Continent
- Region
- Country
- District
- City

### Alternative Flows
- If the selected geographical area does not exist, the system requests another selection.
- If population data cannot be retrieved, the system informs the user that the report cannot be generated.

### Postconditions
- The requested population total and its geographical level are displayed.
- No database data is modified.

---

## UC-06 — Generate Language Population Reports

**Primary Actor:** Organisation User

**Goal:** Compare the number and percentage of people speaking the required major languages.

### Preconditions
- The population reporting system is running.
- The World database is available.
- Language and population data is available.

### Main Flow
1. The user selects the language population report.
2. The system retrieves language data from the World database.
3. The system identifies the required languages:
   - Chinese
   - English
   - Hindi
   - Spanish
   - Arabic
4. The system calculates the number of people speaking each language.
5. The system calculates the percentage of the world's population speaking each language.
6. The system sorts the languages from greatest number of speakers to smallest.
7. The system displays the language population report.
8. The user views the results.

### Report Information
The report displays:
- Language
- Number of speakers
- Percentage of world population

### Alternative Flows
- If required language data is unavailable, the system informs the user that the report cannot be generated.

### Postconditions
- The language population report is displayed.
- No database data is modified.
