CREATE SEQUENCE IF NOT EXISTS group_id_seq START WITH 1 INCREMENT BY 30 CACHE 30; 
CREATE SEQUENCE IF NOT EXISTS user_id_seq START WITH 1 INCREMENT BY 30 CACHE 30; 
CREATE SEQUENCE IF NOT EXISTS classroom_id_seq START WITH 1 INCREMENT BY 30 CACHE 30; 
CREATE SEQUENCE IF NOT EXISTS lesson_id_seq START WITH 1 INCREMENT BY 30 CACHE 30; 


CREATE TABLE IF NOT EXISTS faculties(
faculty_id BIGSERIAL PRIMARY KEY,
faculty_title varchar(225) UNIQUE,
faculty_code varchar(225) UNIQUE
);


CREATE TABLE IF NOT EXISTS departments (
department_id BIGSERIAL PRIMARY KEY,
department_title varchar(225) UNIQUE,
department_code varchar(225) UNIQUE,
faculty_id INTEGER NOT NULL,

CONSTRAINT uk_department_title_faculty UNIQUE (department_title, faculty_id),
CONSTRAINT fk_department_faculty FOREIGN KEY (faculty_id) REFERENCES faculties (faculty_id)
);


CREATE TABLE IF NOT EXISTS specializations (
specialization_id BIGSERIAL PRIMARY KEY,
specialization_title varchar(225) UNIQUE,
specialization_code varchar(225) UNIQUE,
department_id INTEGER NOT NULL,

CONSTRAINT uk_specialization_title_department UNIQUE (specialization_title, department_id),

CONSTRAINT fk_specialization_department FOREIGN KEY (department_id) REFERENCES departments (department_id)
);


CREATE TABLE IF NOT EXISTS academic_groups(
group_id INTEGER PRIMARY KEY,
group_name varchar(225),
specialization_id INTEGER NOT NULL,
year_of_univ INTEGER,
start_date DATE NOT NULL,

CONSTRAINT uk_group_name_specialization UNIQUE (group_name, specialization_id),

CONSTRAINT fk_group_specialization FOREIGN KEY (specialization_id) REFERENCES specializations (specialization_id)
);


CREATE TABLE IF NOT EXISTS users(
user_id INTEGER PRIMARY KEY,
user_first_name varchar(225) NOT NULL,
user_middle_name varchar(225),
user_last_name varchar(225) NOT NULL
);


CREATE TABLE IF NOT EXISTS teachers(
user_id INTEGER PRIMARY KEY,
department_id INTEGER NOT NULL,

CONSTRAINT uk_teacher_department UNIQUE (user_id, department_id),

CONSTRAINT fk_teacher_department FOREIGN KEY (department_id) REFERENCES departments (department_id),
CONSTRAINT fk_teacher_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS subjects(
subject_id BIGSERIAL PRIMARY KEY,
subject_title varchar(225) UNIQUE,
teacher_id  INTEGER NOT NULL,
department_id  INTEGER NOT NULL,

CONSTRAINT uk_subject_title_department UNIQUE (subject_title, department_id),

CONSTRAINT fk_subject_teacher FOREIGN KEY (teacher_id ) REFERENCES teachers (user_id),
CONSTRAINT fk_subject_department FOREIGN KEY (department_id) REFERENCES departments (department_id)

);


CREATE TABLE IF NOT EXISTS students(
user_id INTEGER PRIMARY KEY,
group_id INTEGER NOT NULL,

CONSTRAINT uq_student_group UNIQUE (group_id, user_id),

CONSTRAINT fk_student_group FOREIGN KEY (group_id) REFERENCES academic_groups (group_id),
CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS classrooms(
classroom_id INTEGER PRIMARY KEY,
classroom_name varchar(225) UNIQUE
);


CREATE TABLE IF NOT EXISTS lessons(
lesson_id INTEGER PRIMARY KEY,
subject_id INTEGER NOT NULL,
classroom_id INTEGER NOT NULL,
group_id INTEGER NOT NULL,
teacher_id INTEGER NOT NULL,
class_hours varchar(225) NOT NULL,
week_days varchar(225) NOT NULL,

CONSTRAINT uk_subject_classroom UNIQUE (lesson_id, classroom_id, group_id),

CONSTRAINT fk_lesson_subject FOREIGN KEY (subject_id) REFERENCES subjects (subject_id),
CONSTRAINT fk_lesson_classroom FOREIGN KEY (classroom_id) REFERENCES classrooms (classroom_id),
CONSTRAINT fk_lesson_group FOREIGN KEY (group_id) REFERENCES academic_groups (group_id),
CONSTRAINT fk_lesson_teacher FOREIGN KEY (teacher_id) REFERENCES teachers (user_id)
);


CREATE TABLE IF NOT EXISTS groups_subjects(
group_id INTEGER NOT NULL,
subject_id INTEGER NOT NULL,

CONSTRAINT pk_groups_subjects PRIMARY KEY (group_id, subject_id),
CONSTRAINT fk_groups_subjects_group FOREIGN KEY (group_id) REFERENCES academic_groups(group_id),
CONSTRAINT fk_groups_subjects_subject FOREIGN KEY (subject_id) REFERENCES subjects(subject_id)
);