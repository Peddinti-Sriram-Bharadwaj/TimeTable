-- schema.sql
CREATE TABLE IF NOT EXISTS timeslot (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        day_of_week VARCHAR(10),
    start_time TIME,
    end_time TIME
    );

CREATE TABLE IF NOT EXISTS courses (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       course_name VARCHAR(255) NOT NULL,
    faculty VARCHAR(255),
    room_no VARCHAR(50),
    specialization VARCHAR(255)
    );