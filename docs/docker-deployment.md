# SET08103 — Docker Deployment Guide

## 1. Overview

This document explains how to build, deploy and test the SET08103 Java population reporting application using Maven, Docker and MySQL.

The application uses Java 17, Maven and JDBC to connect to the MySQL World database and generate population reports through a console menu.

## 2. Prerequisites

The following software is required:

- Java 17
- Apache Maven
- Docker Desktop
- Git
- MySQL World database (`world`)

Docker Desktop must be running before executing the deployment commands.

## 3. Build the Java Application

From the project root directory, run:

```powershell
mvn clean verify
```

This compiles the application, runs automated tests and generates the executable JAR file.

**Verified test results:**
- 85 tests passed through Maven Surefire.
- 1 database integration test passed through Maven Failsafe.
- 0 failures, 0 errors and 0 skipped tests.
- Final result: `BUILD SUCCESS`.

The generated JAR is located at:

`target/set08103-coursework-1.0-SNAPSHOT.jar`

## 4. Build the Docker Image

The project Dockerfile uses the Eclipse Temurin Java 17 runtime and copies the Maven-built JAR into the image.

Run:

```powershell
docker build -t set08103-app .
```

The image was successfully built and tagged `set08103-app:latest`.

## 5. Configure MySQL Networking

A MySQL 8.4 container named `world-mysql` was used for the deployment.

Check that the container is running:

```powershell
docker ps
```

Create a dedicated Docker network:

```powershell
docker network create set08103-network
```

Connect the existing MySQL container to the network:

```powershell
docker network connect set08103-network world-mysql
```

If the network or connection already exists, it does not need to be created again.

Both containers must share the same Docker network so the Java application can reach MySQL using the hostname `world-mysql`.

## 6. Configure Database Environment Variables

Create a local file named `deployment.env` in the project root:

```dotenv
DB_URL=jdbc:mysql://world-mysql:3306/world
DB_USER=root
DB_PASSWORD=YOUR_MYSQL_PASSWORD
```

Replace the placeholder with the password for your local MySQL database.

The `deployment.env` file must be excluded from Git using `.gitignore`:

```gitignore
deployment.env
```

Do not commit database passwords or other credentials to GitHub.

The Java application reads these variables using `System.getenv()`.

## 7. Run the Application in Docker

Run the following command from the project root:

```powershell
docker run --rm -it --name set08103-java --network set08103-network --env-file deployment.env set08103-app
```

The application should display:

```text
Successfully connected to the World database!
```

The interactive console menu can then be used to generate reports.

## 8. Deployment Verification

The application was successfully started inside Docker and connected to the MySQL World database.

The following report was tested:

**Top 5 capital cities worldwide by population**

| Capital | Country | Population |
|---|---|---:|
| Seoul | South Korea | 9,981,619 |
| Jakarta | Indonesia | 9,604,900 |
| Ciudad de México | Mexico | 8,591,309 |
| Moscow | Russian Federation | 8,389,200 |
| Tokyo | Japan | 7,980,230 |

The application retrieved and displayed these results through the Docker deployment.

This confirms that the Java application can connect to MySQL, execute SQL queries and generate reports while running inside a Docker container.

## 9. Troubleshooting

### Database connection fails

Check that MySQL is running:

```powershell
docker ps
```

Check that the MySQL container is attached to the deployment network:

```powershell
docker network inspect set08103-network
```

Confirm that `DB_URL` uses `world-mysql`, not `localhost`, because the Java application runs inside a separate container.

### Missing environment variables

Confirm that `deployment.env` exists in the project root and contains `DB_URL`, `DB_USER` and `DB_PASSWORD`.

### Docker image build fails

Run `mvn clean verify` before building the image. The Dockerfile requires the generated JAR to exist in the `target` directory.

### MySQL authentication fails

Check that the database username and password are correct and that the MySQL account is permitted to connect from the application container.

## 10. Conclusion

The SET08103 application was successfully packaged into a Maven JAR, built into a Docker image and executed alongside a MySQL database container.

The deployment was verified by generating a capital city population report through the interactive console application.