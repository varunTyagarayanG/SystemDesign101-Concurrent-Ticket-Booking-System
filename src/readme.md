
# Concurrent Ticket Booking System with Pessimistic Locking

## Project Description

This project is a **multi-threaded ticket booking system** designed to simulate real-time seat bookings for flights while ensuring **data integrity** in a concurrent environment. Using **Java** and **MySQL**, the system handles seat reservations where users can concurrently attempt to book seats without conflicts.

The core functionality relies on **database locking techniques** such as `UPDATE` locks and `SKIP LOCKS` to manage access to seats, ensuring that once a seat is being booked by one user, it cannot be accessed by others. The project also explores **pessimistic locking methods** to avoid race conditions and maintain consistency, even under high concurrency.

By simulating a scenario with **100 concurrent users**, the system demonstrates the importance of proper locking mechanisms in preventing inconsistencies and ensuring a seamless user experience.

## Key Skills Showcased

### 1. **Concurrency and Multi-threading**
   - Managed concurrent users with multi-threading (`ExecutorService`) to handle seat bookings efficiently in a simulated real-world environment.

### 2. **Database Management and Locking Mechanisms**
   - Integrated MySQL for data storage and demonstrated advanced **locking techniques** like **pessimistic locking** and **skip locks** (`FOR UPDATE SKIP LOCKED`) to handle concurrent updates without conflicts.

### 3. **Transaction Management and Data Consistency**
   - Ensured data consistency with **transaction management** (`commit`, `rollback`) to maintain ACID properties, preventing data anomalies in a high-concurrency environment.


## Files and Order of Execution

1. **`FlightCreator`**  
   - Responsible for creating the `trips` table and inserting flight information.

2. **`SeatsCreator`**  
   - Used for creating the `seats` table and initializing the seat information.

3. **`UserInserter`**  
   - Adds user information to the database for booking.

4. **`NoLocking`**  
   - Contains a simple implementation of booking seats without any locking mechanism.

5. **`WorstImplementation`**  
   - Simulates a bad implementation of seat booking without proper concurrency handling.

6. **`TicketBookingSystem`**  
   - Main entry point for the ticket booking system. Handles seat booking with concurrency management.

## Execution Order

1. **First**, run `FlightCreator` to set up flight data.
2. **Then**, run `SeatsCreator` to set up seat data.
3. **Next**, run `UserInserter` to insert user data into the database.
4. **After that**, run `WorstImplementation` to simulate the booking without locks.
5. **Finally**, run `TicketBookingSystem` for the main functionality with proper concurrency handling.

---

Ensure that the database schema and tables are created as needed before running these implementations.

## Prerequisites

Before running the project, ensure that you have the following installed:

- **Java 8+**
- **MySQL Server** (Make sure MySQL is running locally or on a remote server)
- **JDBC** for MySQL (MySQL Connector)

## Setting Up the Project

### 1. **Create the MySQL Database**

Create a new database called `TicketBookingSystem` in MySQL.

```sql
CREATE DATABASE TicketBookingSystem;
```

### 2. **Create the Tables**

#### 2.1. **Create the `trips` Table**

This table stores information about the flights.

```sql
CREATE TABLE IF NOT EXISTS trips (
    flight_id INT AUTO_INCREMENT PRIMARY KEY,
    flight_name VARCHAR(100) NOT NULL
);
```

#### 2.2. **Create the `users` Table**

This table stores information about users who are booking seats.

```sql
CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    user_name VARCHAR(100) NOT NULL
);
```

#### 2.3. **Create the `seats` Table**

This table stores information about the available seats, including which flight they belong to and the user who booked them.

```sql
CREATE TABLE IF NOT EXISTS seats (
    seat_id INT AUTO_INCREMENT PRIMARY KEY,
    seat_name VARCHAR(10) NOT NULL,
    trip_id INT NOT NULL,
    user_id INT DEFAULT NULL,
    FOREIGN KEY (trip_id) REFERENCES trips(flight_id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
);
```

### 3. **Insert Flights, Seats, and Users**

Before running the main application, you'll need to populate the database with sample flights, seats, and users.

#### 3.1. **FlightCreator**

This class inserts a flight into the `trips` table.

```bash
javac FlightCreator.java
java FlightCreator
```

#### 3.2. **SeatsCreator**

This class inserts 100 seats into the `seats` table for the flight created in the `trips` table.

```bash
javac SeatsCreator.java
java SeatsCreator
```

#### 3.3. **UserInserter**

This class inserts 100 random users into the `users` table.

```bash
javac UserInserter.java
java UserInserter
```

### 4. **TicketBookingSystem (Main Application)**

The `TicketBookingSystem` class is responsible for booking seats and printing the final seat assignment.

- The system uses **multi-threading** to allow 100 users to concurrently attempt booking seats.
- It tracks which seats are available or booked using the `FOR UPDATE SKIP LOCKED` SQL feature to prevent race conditions.
- After booking, it prints the seat grid and resets all seats for the next round of bookings.

To run the main application:

```bash
javac TicketBookingSystem.java
java TicketBookingSystem
```

### 5. **Expected Output**

The system will print:

- The availability grid for all seats (Booked seats marked as `X` and available seats marked as `-`).
- The names of users who have booked each seat.
- The total number of booked seats.
- The execution time of the entire process.

### 6. **Resetting Seats**

The system will reset all seats to available (`user_id = NULL`) after completing the booking. You can manually reset seats using the `resetSeats` method.

### 7. **Sample Output**

```text
Seat Availability Grid (X = Booked, - = Available):
- - - - X - - - X - 
- - X - X - - X X - 
X - - X - - - - - X 
...

Seats Booked by Users:
User 1 (1A)
User 2 (1B)
User 3 (2A)
...

Total Booked Seats: 100

Execution Time: 1500 milliseconds
```

---

## Project Files Overview

### 1. **TicketBookingSystem.java**

This is the main application that manages seat bookings. It uses multi-threading to allow concurrent seat booking.

### 2. **FlightCreator.java**

This file is responsible for creating flights in the `trips` table.

### 3. **SeatsCreator.java**

This file creates 100 seats for the flight in the `seats` table.

### 4. **UserInserter.java**

This file inserts 100 random users into the `users` table.

---

## Troubleshooting

If the program doesn't run as expected, try the following steps:

1. **Check MySQL Connection**: Ensure the MySQL server is running and the connection parameters (`URL`, `USER`, `PASSWORD`) are correct.
2. **Check Table Schema**: Ensure that the tables `trips`, `users`, and `seats` are correctly created in the database.
3. **Concurrency Issues**: If seats are not being booked as expected, verify that the `FOR UPDATE SKIP LOCKED` SQL query is working correctly to avoid race conditions.
4. **Execution Time**: The execution time will depend on your system and the number of users. Make sure your machine has enough resources to handle 100 concurrent threads.

---

## Conclusion

This project demonstrates the implementation of a ticket booking system with concurrent seat booking using multi-threading. It showcases how SQL transactions, particularly UPDATE locks and SKIP LOCKS, are utilized to ensure data consistency in a concurrent environment. The application uses pessimistic locking techniques to simulate real-world seat booking for flights, preventing race conditions and ensuring that seat availability is accurately updated across multiple threads. The integration with MySQL for managing flight, seat, and user data highlights how database locking mechanisms can be leveraged in real-world scenarios to manage concurrency effectively.
