# Capital City Population Reports

## SET08103 — Software Engineering Methods
### Code Review 3 — Issue #3

## 1. Overview

The capital city reporting feature allows users to generate population reports using the MySQL World database.

The feature is implemented in Java 17 using JDBC and Maven.

**Implementation:** `CapitalCityReports.java`

**Application interface:** `ConsoleMenu.java`

**GitHub issue:** #3 — Generate capital city population reports

## 2. Implemented Reports

Six capital city population reports are supported:

1. All capital cities worldwide.
2. All capital cities in a selected continent.
3. All capital cities in a selected region.
4. Top N capital cities worldwide.
5. Top N capital cities in a selected continent.
6. Top N capital cities in a selected region.

All reports display:

- Capital city name.
- Country name.
- Capital city population.

Results are ordered by capital city population, from largest to smallest.

## 3. Database Implementation

The reports use the World database tables:

- `country`
- `city`

Capital cities are identified using the relationship:

`country.Capital = city.ID`

This ensures that only officially recorded capital cities are included.

SQL queries use prepared statements for user-supplied parameters, helping prevent SQL injection.

## 4. User Input and Validation

The console menu allows users to select a capital city report and enter the required parameters.

Inputs include:

- Report selection.
- Continent name.
- Region name.
- Number of results (N).

For Top N reports, N must be a positive integer.

Invalid menu choices, invalid numeric input and blank required values are handled by the console interface.

Users can select another report or exit the application.

## 5. Example Output

Example: Top 5 capital cities worldwide.

| Capital City | Country | Population |
|---|---|---|
| Seoul | South Korea | 9,981,619 |
| Jakarta | Indonesia | 9,604,900 |
| Ciudad de México | Mexico | 8,591,309 |
| Moscow | Russian Federation | 8,389,200 |
| Tokyo | Japan | 7,980,230 |

These figures reflect the supplied World sample database rather than current population estimates.

## 6. Automated Testing

JUnit 5 and Mockito are used for automated testing.

### Unit Tests

`CapitalCityReportsTest.java`

Tests include validation of invalid input values.

`ConsoleMenuTest.java`

Tests cover menu selection, report routing and invalid user input.

### Database Integration Tests

`CapitalCityReportsIntegrationTest.java`

Six database integration tests have been implemented.

The integration tests execute against the World database and verify report behaviour.

### Verified GitHub Actions Results

Command:

`mvn --batch-mode clean verify`

Successful CI run on 4 November 2026:

- Surefire tests: 85
- Failsafe tests: 1
- Total tests: 86
- Failures: 0
- Errors: 0
- Skipped: 0

**Build result: BUILD SUCCESS**

The six capital city integration tests were successfully executed in GitHub Actions.

## 7. Continuous Integration

GitHub Actions automatically builds and tests the application.

The workflow:

1. Checks out the repository.
2. Configures Java 17.
3. Starts a MySQL database service.
4. Imports the World database.
5. Imports the separate city-report test database.
6. Executes Maven unit and integration tests.
7. Generates test and coverage reports.
8. Builds the Docker image.

Database connection settings are provided through environment variables:

- `DB_URL`
- `DB_USER`
- `DB_PASSWORD`

The database password is not hardcoded in Java source files.

## 8. Running the Application

Requirements:

- Java 17
- Maven
- MySQL World database
- Docker, if using a containerised database

Configure the database environment variables, then run the application's `Main` class.

The console menu allows the user to choose an available report, enter the required values, view results and return to the menu.

## 9. Known Limitations

- Population figures are historical values from the supplied World dataset.
- Countries without a recorded capital city do not produce capital city rows.
- Reports depend on the database being available and correctly configured.
- Results are based on the contents of the supplied dataset, not live population statistics.

## 10. GitHub Contributions

**PR #22:** Implement capital city population reports with automated tests.

**PR #23:** Add integration tests for capital city reports.

**PR #34:** Enable database integration tests in the CI workflow.

All three pull requests have been merged into `develop`.

The final `develop` GitHub Actions workflow completed successfully.

## 11. Code Review 3 Demonstration

The demonstration will show:

1. Starting the application.
2. Selecting a capital city report.
3. Entering a continent or region.
4. Entering a Top N value.
5. Viewing population results.
6. Handling invalid input.
7. Running another report.
8. Showing successful automated tests in GitHub Actions.

This provides evidence of implementation, testing, database integration and continuous integration for Code Review 3.
