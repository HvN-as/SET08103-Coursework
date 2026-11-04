# SET08103 Coursework — World Population Reports

## Project Overview

This Java application generates population reports using the World database.

Users can select reports through an interactive console menu.

## Technologies

- Java 17
- Maven
- MySQL Connector/J
- JUnit 5
- Mockito
- GitHub Actions

## Database Setup

The World database SQL file is located at:

`database/world.sql`

Import this file into a compatible MySQL database.

The application requires three environment variables:

- `DB_URL` — JDBC database connection URL
- `DB_USER` — Database username
- `DB_PASSWORD` — Database password

Example configuration:

```bash
export DB_URL="jdbc:mysql://localhost:3306/world"
export DB_USER="your_username"
export DB_PASSWORD="your_password"
```

Replace the placeholders with your own database credentials
in your local environment, not in this README.

## Running the Application

Open the project in IntelliJ IDEA.

1. Configure the database environment variables.
2. Ensure the World database is accessible.
3. Open `src/main/java/uk/ac/napier/Main.java`.
4. Run the `Main` class.
5. Select a report from the console menu.

## Available Reports

### Capital City Reports

1. All capital cities worldwide
2. Capital cities by continent
3. Capital cities by region
4. Top N capital cities worldwide
5. Top N capital cities by continent
6. Top N capital cities by region

### City Population Reports

7. All cities worldwide
8. Cities by continent
9. Cities by region
10. Cities by country
11. Cities by district
12. Top N cities worldwide
13. Top N cities by continent
14. Top N cities by region
15. Top N cities by country
16. Top N cities by district

Enter `0` to exit the application.

## City Report Requirements

The city population reports support filtering by world,
continent, region, country and district.

Cities are ordered by population from largest to smallest.

Top N reports allow users to specify how many cities to display.

Each report includes:

- Name
- Country
- District
- Population

Population data is retrieved from the World database.

## Testing

The project uses JUnit and Mockito for automated testing.

Run unit tests with:

```bash
mvn test
```

Database integration tests require the test database configuration
expected by the integration test suite.

GitHub Actions runs the project's configured automated checks.

## Related GitHub Work

- Issue #2 — Generate city population reports
- PR #27 — CityReport implementation, tests and CI integration
- PR #28 — City population reports console menu integration

