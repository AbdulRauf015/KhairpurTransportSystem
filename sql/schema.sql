-- Khairpur Multi-Transport Management System
-- Database Schema

CREATE DATABASE IF NOT EXISTS khairpur_transport;
USE khairpur_transport;

-- Users table (admins and regular users)
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('admin', 'user') DEFAULT 'user',
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20)
);

-- Drivers table
CREATE TABLE IF NOT EXISTS drivers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20),
    license_number VARCHAR(50) UNIQUE NOT NULL,
    vehicle_type ENUM('Bus', 'Car', 'Bike', 'Airline') NOT NULL
);

-- Vehicles table
CREATE TABLE IF NOT EXISTS vehicles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_type ENUM('Bus', 'Car', 'Bike', 'Airline') NOT NULL,
    vehicle_number VARCHAR(50) UNIQUE NOT NULL,
    capacity INT NOT NULL,
    status ENUM('Active', 'Inactive') DEFAULT 'Active'
);

-- Transport routes table
CREATE TABLE IF NOT EXISTS routes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    route_name VARCHAR(100) NOT NULL,
    from_location VARCHAR(100) NOT NULL,
    to_location VARCHAR(100) NOT NULL,
    distance DOUBLE DEFAULT 0,
    fare DOUBLE NOT NULL,
    transport_type ENUM('Bus', 'Car', 'Bike', 'Airline') NOT NULL
);

-- Bookings table
CREATE TABLE IF NOT EXISTS bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    route_id INT NOT NULL,
    vehicle_id INT,
    driver_id INT,
    booking_date DATE NOT NULL,
    journey_date DATE NOT NULL,
    seats INT NOT NULL DEFAULT 1,
    total_fare DOUBLE NOT NULL,
    status ENUM('Confirmed', 'Cancelled', 'Completed') DEFAULT 'Confirmed',
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES routes(id) ON DELETE CASCADE,
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id) ON DELETE SET NULL,
    FOREIGN KEY (driver_id) REFERENCES drivers(id) ON DELETE SET NULL
);

-- Insert default admin user (password: admin123)
INSERT IGNORE INTO users (username, password, role, name, email, phone)
VALUES ('admin', 'admin123', 'admin', 'Administrator', 'admin@khairpur.com', '0300-0000000');

-- Insert sample routes
INSERT IGNORE INTO routes (route_name, from_location, to_location, distance, fare, transport_type) VALUES
('Khairpur - Sukkur Bus', 'Khairpur', 'Sukkur', 45.0, 150.0, 'Bus'),
('Khairpur - Larkana Bus', 'Khairpur', 'Larkana', 80.0, 250.0, 'Bus'),
('Khairpur - Karachi Airline', 'Khairpur', 'Karachi', 450.0, 8000.0, 'Airline'),
('Khairpur - Rohri Car', 'Khairpur', 'Rohri', 35.0, 500.0, 'Car'),
('Khairpur - Gambat Bike', 'Khairpur', 'Gambat', 20.0, 80.0, 'Bike');

-- Insert sample vehicles
INSERT IGNORE INTO vehicles (vehicle_type, vehicle_number, capacity, status) VALUES
('Bus', 'KHP-BUS-001', 40, 'Active'),
('Bus', 'KHP-BUS-002', 35, 'Active'),
('Car', 'KHP-CAR-001', 4, 'Active'),
('Bike', 'KHP-BIKE-001', 1, 'Active'),
('Airline', 'KHP-AIR-001', 150, 'Active');

-- Insert sample drivers
INSERT IGNORE INTO drivers (name, phone, license_number, vehicle_type) VALUES
('Ahmed Khan', '0300-1234567', 'LIC-001', 'Bus'),
('Imran Ali', '0301-2345678', 'LIC-002', 'Car'),
('Sohail Memon', '0302-3456789', 'LIC-003', 'Bike'),
('Farhan Sheikh', '0303-4567890', 'LIC-004', 'Airline');
