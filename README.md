# Employee Management System

A REST API built with Java and Spring Boot to manage employee records, with validation, exception handling, and JWT-based authentication.

## Features

- Create, read, update, and delete employees (CRUD)
- Pagination for employee listing
- Request validation
- Global exception handling
- DTOs for request and response objects
- JWT authentication
- Role-based authorization
- MySQL database integration
- Unit and controller testing

## Tech Stack

- Java
- Spring Boot
- Spring Security
- Spring Data JPA
- MySQL
- Maven
- JUnit 5 and Mockito

## Prerequisites

- JDK compatible with the project
- MySQL
- Maven (or the included Maven Wrapper)

## Setup

1. Clone this repository.
2. Create a MySQL database for the application.
3. Configure the database URL, username, and password using local configuration or environment variables.
4. Configure the JWT signing secret securely.
5. Run the application using IntelliJ or Maven.

Do not commit real passwords, database credentials, or JWT secrets.

## API Examples

### Login

`POST /api/auth/login`

Example request:

```json
{
  "username": "admin",
  "password": "YOUR_PASSWORD"
}
```

Use the returned JWT as a Bearer token for protected endpoints.

### Employee endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| GET | `/api/employees` | List employees |
| GET | `/api/employees/{id}` | Get one employee |
| POST | `/api/employees` | Create an employee |
| PUT | `/api/employees/{id}` | Update an employee |
| DELETE | `/api/employees/{id}` | Delete an employee |

Access depends on the configured roles. Check the project's security configuration for the exact rules.

## Running Tests

Run the test suite using the Maven Wrapper or Maven installed on your machine.

## Future Improvements

- Add more integration tests
- Improve API documentation
- Add deployment configuration