# Request Validator

A Spring Boot application that demonstrates request validation using Hibernate Validator.

## Features

- Request validation using annotations
- Custom validation error handling
- RESTful API endpoints
- Comprehensive validation rules for user data
- System status page

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

1. Clone the repository

2. The project includes a `start.bat` script that configures the Java environment and provides convenient commands:

   ```bash
   # Start the application
   start.bat

   # Run tests
   start.bat mvn test

   # Clean and build the project
   start.bat mvn clean install
   ```

   The script automatically sets `JAVA_HOME` to the required JDK 17 and configures the PATH accordingly.

3. Alternatively, you can use Maven directly if your environment is already configured:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. Once started, you can access:
   - System Status Page: [http://localhost:8080](http://localhost:8080)
   - API Endpoint: [http://localhost:8080/api/v1/validate](http://localhost:8080/api/v1/validate)

## API Endpoints

### Validate User Request
```http
POST /api/v1/validate
```

Example request body:
```json
{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "age": 25,
    "phoneNumber": "+1234567890"
}
```

## Validation Rules

- Name: Required, 2-50 characters
- Email: Required, valid email format
- Age: Required, between 18 and 100
- Phone Number: Optional, valid international format

## Error Handling

The API returns detailed validation errors in the following format:
```json
{
    "fieldName": "Error message"
}
