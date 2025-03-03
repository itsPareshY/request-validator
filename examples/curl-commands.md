# Example CURL Commands

This document provides example CURL commands for testing the Request Validator API. All examples use the default host `localhost` and port `8080`.

## Valid Request Examples

### 1. Valid User Request
```bash
curl -X POST http://localhost:8080/api/v1/validate \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "age": 25,
    "phoneNumber": "+1234567890"
  }'

# Expected Response: 200 OK
# Response body: "Request validation successful for user: John Doe"
```

### 2. Valid Request Without Phone Number (Optional Field)
```bash
curl -X POST http://localhost:8080/api/v1/validate \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Jane Smith",
    "email": "jane.smith@example.com",
    "age": 30
  }'

# Expected Response: 200 OK
# Response body: "Request validation successful for user: Jane Smith"
```

## Invalid Request Examples

### 1. Invalid Email Format
```bash
curl -X POST http://localhost:8080/api/v1/validate \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "invalid-email",
    "age": 25,
    "phoneNumber": "+1234567890"
  }'

# Expected Response: 400 Bad Request
# Response body: {"email": "Invalid email format"}
```

### 2. Name Too Short
```bash
curl -X POST http://localhost:8080/api/v1/validate \
  -H "Content-Type: application/json" \
  -d '{
    "name": "J",
    "email": "john.doe@example.com",
    "age": 25,
    "phoneNumber": "+1234567890"
  }'

# Expected Response: 400 Bad Request
# Response body: {"name": "Name must be between 2 and 50 characters"}
```

### 3. Age Below Minimum
```bash
curl -X POST http://localhost:8080/api/v1/validate \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "age": 17,
    "phoneNumber": "+1234567890"
  }'

# Expected Response: 400 Bad Request
# Response body: {"age": "Age must be at least 18"}
```

### 4. Invalid Phone Number Format
```bash
curl -X POST http://localhost:8080/api/v1/validate \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "age": 25,
    "phoneNumber": "123-456-7890"
  }'

# Expected Response: 400 Bad Request
# Response body: {"phoneNumber": "Invalid phone number format"}
```

### 5. Multiple Validation Errors
```bash
curl -X POST http://localhost:8080/api/v1/validate \
  -H "Content-Type: application/json" \
  -d '{
    "name": "J",
    "email": "invalid-email",
    "age": 15,
    "phoneNumber": "invalid-phone"
  }'

# Expected Response: 400 Bad Request
# Response body: {
#   "name": "Name must be between 2 and 50 characters",
#   "email": "Invalid email format",
#   "age": "Age must be at least 18",
#   "phoneNumber": "Invalid phone number format"
# }
```

## Windows PowerShell Commands

For Windows PowerShell users, use the following format:

```powershell
Invoke-RestMethod -Method Post -Uri "http://localhost:8080/api/v1/validate" `
  -Headers @{"Content-Type"="application/json"} `
  -Body '{
    "name": "John Doe",
    "email": "john.doe@example.com",
    "age": 25,
    "phoneNumber": "+1234567890"
  }'
```

## Testing Tips

1. Start the application using:
   ```bash
   start.bat
   ```

2. Run these curl commands in a separate terminal window
3. For Windows users without curl, use the PowerShell commands provided above
4. Make sure the application is running before executing the commands
5. Check the application logs for detailed information about validation failures
