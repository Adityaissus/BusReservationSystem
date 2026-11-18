CREATE DATABASE IF NOT EXISTS bus_reservation;
USE bus_reservation;

DROP TABLE IF EXISTS tickets;
DROP TABLE IF EXISTS routes;
DROP TABLE IF EXISTS buses;

CREATE TABLE buses (
    bus_id INT PRIMARY KEY AUTO_INCREMENT,
    bus_name VARCHAR(100) NOT NULL,
    bus_type VARCHAR(30) NOT NULL,
    total_seats INT NOT NULL,
    CHECK (total_seats > 0)
);

CREATE TABLE routes (
    route_id INT PRIMARY KEY AUTO_INCREMENT,
    bus_id INT NOT NULL,
    source VARCHAR(100) NOT NULL,
    destination VARCHAR(100) NOT NULL,
    departure_time TIME NOT NULL,
    arrival_time TIME NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    CONSTRAINT fk_routes_bus FOREIGN KEY (bus_id) REFERENCES buses(bus_id),
    CHECK (price >= 0)
);

CREATE TABLE tickets (
    ticket_id BIGINT PRIMARY KEY AUTO_INCREMENT,
    bus_id INT NOT NULL,
    route_id INT NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    gender VARCHAR(20) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    seat_number INT NOT NULL,
    journey_date DATE NOT NULL,
    ticket_price DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'CONFIRMED',
    booked_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_tickets_bus FOREIGN KEY (bus_id) REFERENCES buses(bus_id),
    CONSTRAINT fk_tickets_route FOREIGN KEY (route_id) REFERENCES routes(route_id),
    CONSTRAINT uq_bus_date_seat UNIQUE (bus_id, journey_date, seat_number),
    CHECK (age > 0),
    CHECK (seat_number > 0),
    CHECK (status IN ('CONFIRMED', 'CANCELLED'))
);

INSERT INTO buses (bus_name, bus_type, total_seats) VALUES
('Volvo Express', 'AC', 40),
('Himachal Express', 'NON_AC', 36),
('Night Rider', 'SLEEPER', 30),
('City Link', 'AC', 32);

INSERT INTO routes (bus_id, source, destination, departure_time, arrival_time, price) VALUES
(1, 'Delhi', 'Chandigarh', '08:00:00', '12:00:00', 800.00),
(2, 'Kangra', 'Delhi', '19:00:00', '06:00:00', 600.00),
(3, 'Delhi', 'Manali', '21:00:00', '08:00:00', 1200.00),
(4, 'Chandigarh', 'Kangra', '09:30:00', '15:30:00', 650.00);
