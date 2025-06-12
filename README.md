# Vehicle Management System 🚗

A Spring JDBC-based Vehicle Management System that allows users to manage vehicle-related data efficiently. This project demonstrates the use of Java, Spring Framework, and MySQL in a layered architecture.

## Technologies Used

- Java (JDK 8+)
- Spring Framework (v4.3.0)
- Spring JDBC
- MySQL
- Maven
- Eclipse IDE

## Features

- Add, update, delete, and view vehicle records
- Layered architecture: Controller → Service → DAO
- JDBC Template integration for database access
- MySQL database support
- Basic CRUD operations with clean separation of concerns

## How to Run

1. Clone the repository
   ```bash
   git clone https://github.com/nithishkumarvemu/VehicleManagementSystem.git
   cd VehicleManagementSystem
Set up MySQL database

sql
CREATE DATABASE vehicle_db;
USE vehicle_db;
CREATE TABLE vehicle (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100),
    type VARCHAR(50),
    model VARCHAR(100),
    registration_no VARCHAR(50)
);
Update DB config in applicationContext.xml
xml
<property name="url" value="jdbc:mysql://localhost:3306/vehicle_db"/>
<property name="username" value="your_mysql_username"/>
<property name="password" value="your_mysql_password"/>
Run the application

Import the project into Eclipse as a Maven project.

Run the main class to interact with the system.

Author
Nithish Kumar Vemu
📍 Arlington, Texas
📧 vemunithishkumar@gmail.com

License
This project is licensed under the MIT License.
