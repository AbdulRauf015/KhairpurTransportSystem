-- ============================================
-- Khairpur Multi-Transport Management System
-- Developer: Abdul Rauf
-- FIXED VERSION - Sab Kuch Reset Kar Dega
-- ============================================

-- Database banao
CREATE DATABASE IF NOT EXISTS khairpur_transport;
USE khairpur_transport;

-- Pehle sab tables hatao taake fresh start ho
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS vehicles;
DROP TABLE IF EXISTS drivers;
DROP TABLE IF EXISTS transport_routes;
SET FOREIGN_KEY_CHECKS = 1;

-- ── USERS TABLE ──
CREATE TABLE users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role ENUM('admin','user') NOT NULL DEFAULT 'user',
    full_name VARCHAR(100) NOT NULL,
    contact VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- ── TRANSPORT ROUTES TABLE ──
CREATE TABLE transport_routes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transport_type ENUM('Bus','Car','Bike','Airline') NOT NULL,
    from_city VARCHAR(100) NOT NULL,
    to_city VARCHAR(100) NOT NULL,
    distance_km INT DEFAULT 0,
    fare DECIMAL(10,2) NOT NULL,
    duration VARCHAR(50),
    status ENUM('Active','Inactive') DEFAULT 'Active'
);

-- ── VEHICLES TABLE ──
CREATE TABLE vehicles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    transport_type ENUM('Bus','Car','Bike','Airline') NOT NULL,
    vehicle_number VARCHAR(30) NOT NULL UNIQUE,
    model VARCHAR(100),
    capacity INT DEFAULT 1,
    status ENUM('Active','Inactive','Maintenance') DEFAULT 'Active'
);

-- ── DRIVERS TABLE ──
CREATE TABLE drivers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    license_number VARCHAR(50) NOT NULL UNIQUE,
    contact VARCHAR(20) NOT NULL,
    transport_type ENUM('Bus','Car','Bike','Airline') NOT NULL,
    address VARCHAR(200),
    status ENUM('Active','Inactive') DEFAULT 'Active'
);

-- ── BOOKINGS TABLE ──
CREATE TABLE bookings (
    id INT AUTO_INCREMENT PRIMARY KEY,
    booking_number VARCHAR(30) NOT NULL UNIQUE,
    transport_type ENUM('Bus','Car','Bike','Airline') NOT NULL,
    passenger_name VARCHAR(100) NOT NULL,
    passenger_contact VARCHAR(20),
    route_id INT NOT NULL,
    vehicle_id INT NOT NULL,
    driver_id INT,
    travel_date DATE NOT NULL,
    fare DECIMAL(10,2) NOT NULL,
    seat_number VARCHAR(10),
    special_note VARCHAR(200),
    status ENUM('Booked','Cancelled','Completed') DEFAULT 'Booked',
    booked_by INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (route_id) REFERENCES transport_routes(id),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id),
    FOREIGN KEY (driver_id) REFERENCES drivers(id),
    FOREIGN KEY (booked_by) REFERENCES users(id)
);

-- ── USERS DATA ──
INSERT INTO users (username,password,role,full_name,contact) VALUES
('admin','admin123','admin','Abdul Rauf','03001234500'),
('user1','user123','user','Ali Hassan','03001234501'),
('user2','user123','user','Sara Khan','03001234502'),
('user3','user123','user','Ahmed Ali','03001234503');

-- ── ROUTES - BUS ──
INSERT INTO transport_routes (transport_type,from_city,to_city,distance_km,fare,duration,status) VALUES
('Bus','Khairpur','Sukkur',80,150,'2 Hours','Active'),
('Bus','Khairpur','Larkana',120,200,'3 Hours','Active'),
('Bus','Khairpur','Nawabshah',150,250,'4 Hours','Active'),
('Bus','Khairpur','Hyderabad',280,450,'6 Hours','Active'),
('Bus','Khairpur','Karachi',450,700,'9 Hours','Active'),
('Bus','Khairpur','Rohri',30,80,'45 Mins','Active'),
('Bus','Khairpur','Jacobabad',100,180,'2.5 Hours','Active'),
('Bus','Khairpur','Shikarpur',90,160,'2 Hours','Active'),
('Bus','Sukkur','Karachi',380,600,'8 Hours','Active'),
('Bus','Khairpur','Sukkur Airport',85,160,'2 Hours','Active');

-- ── ROUTES - CAR ──
INSERT INTO transport_routes (transport_type,from_city,to_city,distance_km,fare,duration,status) VALUES
('Car','Khairpur','Sukkur',80,800,'1.5 Hours','Active'),
('Car','Khairpur','Larkana',120,1200,'2 Hours','Active'),
('Car','Khairpur','Nawabshah',150,1500,'2.5 Hours','Active'),
('Car','Khairpur','Hyderabad',280,2800,'4 Hours','Active'),
('Car','Khairpur','Karachi',450,4500,'7 Hours','Active'),
('Car','Khairpur','Sukkur Airport',85,900,'1.5 Hours','Active'),
('Car','Khairpur','Rohri',30,350,'40 Mins','Active');

-- ── ROUTES - BIKE ──
INSERT INTO transport_routes (transport_type,from_city,to_city,distance_km,fare,duration,status) VALUES
('Bike','Khairpur','Sukkur',80,300,'2 Hours','Active'),
('Bike','Khairpur','Rohri',30,150,'45 Mins','Active'),
('Bike','Khairpur','Sukkur Airport',85,320,'2 Hours','Active'),
('Bike','Khairpur','Jacobabad',100,400,'2.5 Hours','Active'),
('Bike','Khairpur','Shikarpur',90,350,'2 Hours','Active');

-- ── ROUTES - AIRLINE ──
INSERT INTO transport_routes (transport_type,from_city,to_city,distance_km,fare,duration,status) VALUES
('Airline','Sukkur Airport','Karachi Airport',450,8500,'1 Hour','Active'),
('Airline','Sukkur Airport','Lahore Airport',950,12000,'1.5 Hours','Active'),
('Airline','Sukkur Airport','Islamabad Airport',1100,14000,'2 Hours','Active'),
('Airline','Sukkur Airport','Quetta Airport',500,9000,'1.5 Hours','Active'),
('Airline','Sukkur Airport','Dubai Airport',2200,35000,'3 Hours','Active');

-- ── VEHICLES - BUSES ──
INSERT INTO vehicles (transport_type,vehicle_number,model,capacity,status) VALUES
('Bus','KHP-B001','Daewoo Express',45,'Active'),
('Bus','KHP-B002','Faisal Movers',45,'Active'),
('Bus','KHP-B003','Skyways AC',40,'Active'),
('Bus','KHP-B004','Road Master',40,'Active'),
('Bus','KHP-B005','Mini Coach',25,'Active'),
('Bus','KHP-B006','Daewoo Express',45,'Active'),
('Bus','KHP-B007','Faisal Movers',45,'Active'),
('Bus','KHP-B008','AC Deluxe',50,'Active'),
('Bus','KHP-B009','Standard',40,'Active'),
('Bus','KHP-B010','Mini Bus',30,'Active'),
('Bus','KHP-B011','Daewoo Express',45,'Active'),
('Bus','KHP-B012','Road Master',40,'Active'),
('Bus','KHP-B013','AC Coach',45,'Active'),
('Bus','KHP-B014','Standard',40,'Active'),
('Bus','KHP-B015','Mini Bus',30,'Active');

-- ── VEHICLES - CARS ──
INSERT INTO vehicles (transport_type,vehicle_number,model,capacity,status) VALUES
('Car','KHP-C001','Toyota Corolla',4,'Active'),
('Car','KHP-C002','Honda City',4,'Active'),
('Car','KHP-C003','Suzuki Cultus',4,'Active'),
('Car','KHP-C004','Toyota Fortuner',6,'Active'),
('Car','KHP-C005','Honda Civic',4,'Active'),
('Car','KHP-C006','Toyota Prado',7,'Active'),
('Car','KHP-C007','Suzuki Alto',4,'Active'),
('Car','KHP-C008','Toyota Corolla',4,'Active'),
('Car','KHP-C009','Honda BRV',6,'Active'),
('Car','KHP-C010','Suzuki Wagon R',5,'Active');

-- ── VEHICLES - BIKES ──
INSERT INTO vehicles (transport_type,vehicle_number,model,capacity,status) VALUES
('Bike','KHP-K001','Honda CD 70',1,'Active'),
('Bike','KHP-K002','Yamaha YBR',1,'Active'),
('Bike','KHP-K003','Honda CG 125',1,'Active'),
('Bike','KHP-K004','Suzuki GS 150',1,'Active'),
('Bike','KHP-K005','Honda CB 150',1,'Active');

-- ── VEHICLES - AIRLINES ──
INSERT INTO vehicles (transport_type,vehicle_number,model,capacity,status) VALUES
('Airline','PK-301','PIA Boeing 737',150,'Active'),
('Airline','PK-302','PIA ATR 72',70,'Active'),
('Airline','FL-101','AirBlue A320',170,'Active'),
('Airline','SX-201','SereneAir B738',160,'Active'),
('Airline','PK-303','PIA Boeing 777',300,'Active');

-- ── DRIVERS - BUS ──
INSERT INTO drivers (name,license_number,contact,transport_type,address,status) VALUES
('Muhammad Aslam','BUS-001','03001234501','Bus','Khairpur','Active'),
('Abdul Karim','BUS-002','03001234502','Bus','Sukkur','Active'),
('Ghulam Hussain','BUS-003','03001234503','Bus','Khairpur','Active'),
('Noor Muhammad','BUS-004','03001234504','Bus','Larkana','Active'),
('Zulfiqar Ali','BUS-005','03001234505','Bus','Khairpur','Active'),
('Bashir Ahmed','BUS-006','03001234506','Bus','Sukkur','Active'),
('Manzoor Hussain','BUS-007','03001234507','Bus','Rohri','Active'),
('Riaz Ahmed','BUS-008','03001234508','Bus','Khairpur','Active'),
('Iqbal Hussain','BUS-009','03001234509','Bus','Nawabshah','Active'),
('Shahid Mehmood','BUS-010','03001234510','Bus','Larkana','Active');

-- ── DRIVERS - CAR ──
INSERT INTO drivers (name,license_number,contact,transport_type,address,status) VALUES
('Tariq Mahmood','CAR-001','03001234511','Car','Khairpur','Active'),
('Javed Iqbal','CAR-002','03001234512','Car','Sukkur','Active'),
('Pervez Akhtar','CAR-003','03001234513','Car','Khairpur','Active'),
('Waqar Ahmed','CAR-004','03001234514','Car','Nawabshah','Active'),
('Nasir Ali','CAR-005','03001234515','Car','Khairpur','Active');

-- ── DRIVERS - BIKE ──
INSERT INTO drivers (name,license_number,contact,transport_type,address,status) VALUES
('Imran Khan','BIKE-001','03001234516','Bike','Khairpur','Active'),
('Asif Raza','BIKE-002','03001234517','Bike','Khairpur','Active'),
('Usman Ghani','BIKE-003','03001234518','Bike','Sukkur','Active'),
('Kamran Akbar','BIKE-004','03001234519','Bike','Khairpur','Active'),
('Farhan Saeed','BIKE-005','03001234520','Bike','Rohri','Active');

-- ── DRIVERS - AIRLINE ──
INSERT INTO drivers (name,license_number,contact,transport_type,address,status) VALUES
('Capt. Ahmed Siddiqui','PIL-001','03001234521','Airline','Karachi','Active'),
('Capt. Hassan Raza','PIL-002','03001234522','Airline','Lahore','Active'),
('Capt. Bilal Khan','PIL-003','03001234523','Airline','Islamabad','Active'),
('Capt. Faisal Nawaz','PIL-004','03001234524','Airline','Karachi','Active'),
('Capt. Salman Butt','PIL-005','03001234525','Airline','Lahore','Active');

-- ── SAMPLE BOOKINGS ──
INSERT INTO bookings (booking_number,transport_type,passenger_name,passenger_contact,route_id,vehicle_id,driver_id,travel_date,fare,seat_number,status,booked_by) VALUES
('BK-0001','Bus','Ahmed Ali','03001111111',1,1,1,CURDATE(),150,'A1','Booked',2),
('BK-0002','Car','Sara Khan','03002222222',11,16,11,CURDATE(),800,'','Booked',3),
('BK-0003','Bike','Hassan Raza','03003333333',18,21,16,CURDATE(),300,'','Booked',2),
('BK-0004','Airline','Fatima Noor','03004444444',23,26,21,CURDATE(),8500,'12A','Booked',4);

-- ============================================
-- DONE! Login karo:
-- Username: admin
-- Password: admin123
-- ============================================
