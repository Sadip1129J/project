create database traffic_fine;
use traffic_fine;
-- Create Users Table
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('Admin', 'traffic', 'User') NOT NULL
);

-- Create Fines Table
CREATE TABLE fines (
    fine_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT,
    amount DECIMAL(10, 2) NOT NULL,
    violation VARCHAR(255) NOT NULL,
    issue_date DATE NOT NULL,
    status ENUM('Pending', 'Paid', 'Disputed') NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(user_id)
);
ALTER TABLE fines ADD CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users(user_id);
SHOW COLUMNS FROM fines;
SELECT * FROM fines;
 SHOW COLUMNS FROM fines;
 ALTER TABLE fines DROP FOREIGN KEY fk_user_id;
ALTER TABLE fines ADD CONSTRAINT fk_user_id FOREIGN KEY (`user_id`) REFERENCES users(`user_id`);
SELECT user_id FROM fines;



delete from fines;

SET SQL_SAFE_UPDATES = 0;
DELETE FROM fines;
SET SQL_SAFE_UPDATES = 1;





SELECT * FROM fines LIMIT 1;
FLUSH TABLE fines;
 SELECT * FROM information_schema.KEY_COLUMN_USAGE WHERE TABLE_NAME = 'fines'; 

-- Create Payments Table
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    fine_id INT,
    payment_date DATE NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (fine_id) REFERENCES fines(fine_id)
);



-- Create Disputes Table
CREATE TABLE disputes (
    dispute_id INT AUTO_INCREMENT PRIMARY KEY,
    fine_id INT,
    dispute_date DATE NOT NULL,
    reason TEXT NOT NULL,
    status ENUM('Pending', 'Resolved') NOT NULL,
    FOREIGN KEY (fine_id) REFERENCES fines(fine_id)
);

-- Create Reports Table (for Admin)
CREATE TABLE reports (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    generated_by INT,
    report_date DATE NOT NULL,
    report_type VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    FOREIGN KEY (generated_by) REFERENCES users(user_id)
);



-- Insert Sample Users
INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'Admin'), 
('operator1', 'operator123', 'Operator'),
('mangouser1', 'mango123', 'Mango User');

INSERT INTO users (username, password, role) VALUES
('admin', 'admin123', 'Admin'), 
('operator1', 'operator123', 'Traffic'),  -- Changed 'Operator' to 'Traffic'
('mangouser1', 'mango123', 'User ');       -- Changed 'Mango User' to 'User '

-- Insert Sample Fines
INSERT INTO fines (user_id, amount, violation, issue_date, status) VALUES
(3, 100.00, 'Speeding', '2023-01-01', 'Pending'),
(3, 50.00, 'Parking Violation', '2023-02-01', 'Paid');

-- Insert Sample Payments
INSERT INTO payments (fine_id, payment_date, amount) VALUES
(2, '2023-02-05', 50.00);

-- Insert Sample Disputes
INSERT INTO disputes (fine_id, dispute_date, reason, status) VALUES
(1, '2023-01-05', 'Incorrect speed measurement', 'Pending');

-- Insert Sample Reports
INSERT INTO reports (generated_by, report_date, report_type, content) VALUES
(1, '2023-03-01', 'Monthly Fine Report', 'Total fines issued: 2, Total payments received: 1');

select * from users;
select * from fines;
select * from payments;
select * from disputes;
select * from reports;

use traffic_fine;
create table test(
name varchar(50),
description varchar(50),
type varchar(50),
location varchar(50)
);
show tables;
select * from payments;
alter table payments add column status varchar(30);
 INSERT INTO users (username, password, role) VALUES('baibhav','aaa','User');
 ALTER TABLE users MODIFY role VARCHAR(100);
 
 
UPDATE users
SET role = "User"
WHERE role = " User" AND user_id IS NOT NULL;
SET SQL_SAFE_UPDATES = 0;
 
 update users set role="Traffic" where role="operator";
 select * from fines;
 
 select * from users
 