# Student Management Backend

This is a Spring Boot application for managing students. It provides a REST API for CRUD operations on student entities.

## Table of Contents

- [Features](#features)
- [Requirements](#requirements)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Running Tests](#running-tests)


## Features

- Create, read, update, and delete students
- REST API
- MySQL database integration
- Docker support

## Requirements

- Java 21
- Spring Boot 3.4.3
- Spring Data JPA
- Actuator
- Swagger
- Validation
- Lombok
- Model Mapper
- Maven
- MySQL
- Git
- GitHub
- Docker
- Logging with SLF4J
- Resilience4j for fault tolerance
- Rate Limiting with resilience4j
- Exception handling

## Installation

1. Clone the repository:
    git clone https://github.com/ruben-rdez/student-mgmt.git

2. Configure the database in application.properties:
    spring.datasource.url=jdbc:mysql://localhost:3306/studentsdb
    spring.datasource.driverClassName=com.mysql.cj.jdbc.Driver
    spring.datasource.username=username
    spring.datasource.password=password

3. Build the project:
   mvn clean package

## Usage

1. Run the application:
   mvn spring-boot:run

2. The application will be available at http://localhost:8080/swagger-ui/index.html

## API Endpoints

- `GET /students` - Get all students
- `GET /students/{id}` - Get a student by id
- `GET /students/{email}` - Get a student by email
- `POST /students` - Add a student
- `PUT /students/{id}` - Update a student
- `PATCH /students/{id}/email` - Update a student's email by id
- `DELETE /students/{id}` - Delete a student

## Running Tests

To run the tests, use the following command:
mvn test