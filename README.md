# Timetable EPR Slice

This is a Spring Boot application for managing student timetables and courses. The system allows students to register for courses, view their timetable, and handle authentication. It also provides functionalities to manage students, courses, and related data with role-based security.

## Features
- **Login**: Allows students to log in using their credentials.
- **Course Management**: Students can view and register for available courses.
- **Timetable Management**: Students can view their weekly timetable.
- **Security**: Uses JWT (JSON Web Tokens) for secure user authentication.
- **Database**: Uses a relational database to store student, course, and timetable data.

## Technologies Used
- **Spring Boot**: Framework for building the backend API.
- **Spring Security**: For securing the application with JWT.
- **Spring Data JPA**: For database interaction with repositories.
- **H2 Database**: For local development (can be replaced with other databases in production).
- **Thymeleaf**: For rendering server-side templates (optional).
- **JUnit**: For unit and integration tests.

## Setup

Prerequisites
Make sure you have the following installed:

JDK 11 or higher
Maven or Gradle (depending on your build tool)
IDE (e.g., IntelliJ IDEA, Eclipse)
H2 Database for local development (default database in this app)
Installation
Clone this repository:
git clone <repository_url>
cd timetable-management
Install dependencies using Maven or Gradle:
mvn install
Start the Spring Boot application:
mvn spring-boot:run
Access the application by navigating to http://localhost:8080 in your browser.
Configuration
You can configure your database and other settings in the application.properties file located in src/main/resources. By default, this project uses H2 as an in-memory database for development.

Example:

spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
Database Initialization
The data.sql file contains SQL commands that will populate the database with initial data (such as sample courses or students) when the application starts. You can modify this file as needed.

Security
The application uses JWT for authentication and authorization. The SecurityConfig.java class configures Spring Security to use JWT tokens.
Make sure to provide the correct JWT token when making requests to secure endpoints.
API Endpoints

POST /api/v1/auth/login: Login and get a JWT token.

Request Body:

{
  "email": "student@example.com",
  "password": "password"
}

Response:

{
  "token": "your-jwt-token",
  "studentId": "student-123"
}

This token is valid for an hour.



## License

This project is licensed under the MIT License - see the LICENSE file for details.


---

This README provides a comprehensive overview of the Spring Boot project, explaining its structure, setup, and configuration. Let me know if you need any further customization or additional details!





