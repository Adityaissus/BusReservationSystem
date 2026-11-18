# WayPoint — Bus Reservation System

A Java 21 desktop bus reservation application built with Swing, JDBC and MySQL.

## Features
- Modern dashboard with sidebar navigation
- Route search by source, destination and journey date
- Bus service cards with timings, bus type, seat count and fare
- Visual seat picker (available, selected and unavailable states)
- Passenger details and booking confirmation
- Ticket cancellation and refund display
- Passenger manifest lookup by bus ID and journey date

## Requirements
- JDK 21+
- Maven 3.9+
- MySQL 8+

## Database setup
1. Create the database and tables using `database/bus_reservation.sql`.
2. Configure the MySQL URL, username and password in `src/main/java/com/busreservation/database/DBConnection.java` for your local environment.
3. Ensure the MySQL service is running.

## Run
From the project root:

```bash
mvn clean compile exec:java
```

`mvn clean` is important when replacing an older build because it removes stale compiled Swing classes from `target/`.

## Notes
The presentation layer was redesigned. Existing model, service, DAO-style JDBC access, and database schema were retained. Live database operations require a correctly configured local MySQL instance and the supplied schema.
# BusReservationSystem
