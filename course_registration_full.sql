-- Course Registration System - Full SQL Queries
-- Database: course_registration

-- ============================================
-- CREATE DATABASE
-- ============================================
CREATE DATABASE IF NOT EXISTS course_registration;
USE course_registration;

-- ============================================
-- CREATE TABLES
-- ============================================

-- Students Table
CREATE TABLE students (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fullname VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255),
    phone VARCHAR(15),
    degree VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Courses Table
CREATE TABLE courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    course_code VARCHAR(20) NOT NULL UNIQUE,
    course_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL,
    instructor VARCHAR(100) NOT NULL,
    schedule VARCHAR(100) NOT NULL
);

-- Student Courses Table (Many-to-Many relationship)
CREATE TABLE student_courses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL,
    course_id INT NOT NULL,
    registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY unique_enrollment (username, course_id),
    FOREIGN KEY (course_id) REFERENCES courses(id)
);

-- ============================================
-- INSERT COURSES DATA
-- ============================================
INSERT INTO courses (course_code, course_name, credentials, instructor, schedule) VALUES
('CS101', 'Introduction to Computer Science', 3, 'Dr. Smith', 'Mon, Wed 10:00-11:30'),
('CS102', 'Data Structures & Algorithms', 4, 'Dr. Johnson', 'Tue, Thu 14:00-15:30'),
('MATH201', 'Linear Algebra', 3, 'Dr. Williams', 'Mon, Wed 14:00-15:30'),
('CS201', 'Database Management', 3, 'Dr. Brown', 'Tue, Fri 09:00-10:30'),
('CS202', 'Web Development', 3, 'Dr. Davis', 'Wed, Fri 11:00-12:30'),
('CS301', 'Operating Systems', 4, 'Dr. Miller', 'Mon, Wed 16:00-17:30'),
('CS302', 'Computer Networks', 3, 'Dr. Wilson', 'Tue, Thu 11:00-12:30'),
('CS401', 'Machine Learning', 4, 'Dr. Taylor', 'Wed, Fri 14:00-15:30');

-- ============================================
-- INSERT STUDENTS DATA
-- ============================================
INSERT INTO students (fullname, email, username, password, phone, degree) VALUES
('Madhesh. S', 'smadhesh456@gmail.com', 'student', 'encrypted_pass', '0912353563', 'B.Tech'),
('riyas', 'riyas@gmail.com', 'riyas1', 'encrypted_pass', '1234567890', 'B.Tech'),
('Test User', 'test@test.com', 'testuser123', 'encrypted_pass', '9876543210', 'B.Tech'),
('ajay', 'ajay@gmail.com', 'ajay1', 'encrypted_pass', '1234567890', 'B.Tech'),
('arun', 'arun@gmail.com', 'arun1', 'encrypted_pass', '9999999999', 'B.Tech'),
('arunnn', 'arunn@gmail.com', 'arun2', 'encrypted_pass', '0987654321', 'M.Sc'),
('eswary', 'eswary@gmail.com', 'eswary123', 'encrypted_pass', '9943491579', 'B.Tech'),
('MADHESH S', 'smadhesh@gmail.com', 'Madhesh_S', 'encrypted_pass', '9123535638', 'B.Tech'),
('Madhesh. S', 'smadhesh4@gmail.com', 'madhesh_1', 'encrypted_pass', '9123535638', 'BE');

-- ============================================
-- INSERT STUDENT COURSE REGISTRATIONS
-- ============================================
INSERT INTO student_courses (username, course_id) VALUES
('arun2', 1),
('arun2', 2),
('arun2', 3),
('eswary123', 1),
('Madhesh_S', 1),
('Madhesh_S', 2),
('Madhesh_S', 3),
('madhesh_1', 1);

-- ============================================
-- SAMPLE QUERIES
-- ============================================

-- Get all students
SELECT * FROM students;

-- Get all courses
SELECT * FROM courses;

-- Get student course registrations
SELECT s.username, c.course_name, c.instructor 
FROM student_courses sc
JOIN students s ON sc.username = s.username
JOIN courses c ON sc.course_id = c.id;

-- Get courses for a specific student
SELECT c.* FROM courses c
JOIN student_courses sc ON c.id = sc.course_id
WHERE sc.username = 'Madhesh_S';

-- Count students per course
SELECT c.course_name, COUNT(sc.id) as student_count
FROM courses c
LEFT JOIN student_courses sc ON c.id = sc.course_id
GROUP BY c.id, c.course_name;

-- Register student for course
INSERT INTO student_courses (username, course_id) VALUES ('username', 1);

-- Unregister student from course
DELETE FROM student_courses WHERE username = 'username' AND course_id = 1;

-- Update student
UPDATE students SET phone = '1234567890', degree = 'B.Tech' WHERE username = 'student';

-- Delete student
DELETE FROM students WHERE username = 'student';