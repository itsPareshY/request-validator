@echo off
:: This script sets up the Java environment and provides two modes of operation:
:: 1. Without arguments: Starts the Spring Boot application
:: 2. With arguments: Runs the specified command with the configured Java environment
::
:: Usage:
::   start.bat             - Starts the Spring Boot application
::   start.bat mvn test    - Runs the test suite
::   start.bat <command>   - Runs any command with the configured Java environment
::
:: Examples:
::   start.bat                     - Starts the application on port 8080
::   start.bat mvn test           - Runs all tests
::   start.bat mvn clean install  - Cleans and builds the project
::

SET JAVA_HOME=C:\jdk-17.0.14
SET PATH=%JAVA_HOME%\bin;%PATH%

echo Java environment configured.
IF "%1"=="" (
    echo Starting application...
    mvn spring-boot:run
) ELSE (
    echo Running command: %*
    %*
)
