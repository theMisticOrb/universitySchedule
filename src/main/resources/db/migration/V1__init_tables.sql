CREATE SEQUENCE IF NOT EXISTS faculty_id_seq START WITH 1 INCREMENT BY 1 CACHE 30; 
CREATE SEQUENCE IF NOT EXISTS department_id_seq START WITH 1 INCREMENT BY 1 CACHE 30; 
CREATE SEQUENCE IF NOT EXISTS specialization_id_seq START WITH 1 INCREMENT BY 1 CACHE 30; 
CREATE SEQUENCE IF NOT EXISTS group_id_seq START WITH 1 INCREMENT BY 1 CACHE 30; 
CREATE SEQUENCE IF NOT EXISTS subject_id_seq START WITH 1 INCREMENT BY 1 CACHE 30; 
CREATE SEQUENCE IF NOT EXISTS user_id_seq START WITH 1 INCREMENT BY 1 CACHE 30; 


CREATE TABLE IF NOT EXISTS faculties(
faculty_id INTEGER PRIMARY KEY DEFAULT nextval('faculty_id_seq'),
faculty_title varchar(225) UNIQUE
);


CREATE TABLE IF NOT EXISTS departments (
department_id INTEGER PRIMARY KEY DEFAULT nextval('department_id_seq'),
department_title varchar(225) UNIQUE,
faculty_id INTEGER NOT NULL,

CONSTRAINT fk_department_faculty FOREIGN KEY (faculty_id) REFERENCES faculties (faculty_id)
);


CREATE TABLE IF NOT EXISTS specializations (
specialization_id INTEGER PRIMARY KEY DEFAULT nextval('specialization_id_seq'),
specialization_title varchar(225) UNIQUE,
department_id INTEGER NOT NULL,

CONSTRAINT fk_specialization_department FOREIGN KEY (department_id) REFERENCES departments (department_id)
);


CREATE TABLE IF NOT EXISTS academic_groups(
group_id INTEGER PRIMARY KEY DEFAULT nextval('group_id_seq'),
group_name varchar(225),
faculty_id INTEGER NOT NULL,
department_id INTEGER NOT NULL,
specialization_id INTEGER NOT NULL,
year_of_univ INTEGER NOT NULL,

CONSTRAINT uk_group_name_faculty UNIQUE (group_name, faculty_id),

CONSTRAINT fk_group_faculty FOREIGN KEY (faculty_id) REFERENCES faculties (faculty_id),
CONSTRAINT fk_group_department FOREIGN KEY (department_id) REFERENCES departments (department_id),
CONSTRAINT fk_group_specialization FOREIGN KEY (specialization_id) REFERENCES specializations (specialization_id)
);


CREATE TABLE IF NOT EXISTS users(
user_id INTEGER PRIMARY KEY DEFAULT nextval('user_id_seq'),
user_first_name varchar(225) NOT NULL,
user_middle_name varchar(225),
user_last_name varchar(225) NOT NULL
);


CREATE TABLE IF NOT EXISTS teachers(
user_id INTEGER PRIMARY KEY,
department_id INTEGER NOT NULL,

CONSTRAINT fk_teacher_department FOREIGN KEY (department_id) REFERENCES departments (department_id),
CONSTRAINT fk_teacher_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS subjects(
subject_id INTEGER PRIMARY KEY DEFAULT nextval('subject_id_seq'),
subject_title varchar(225) UNIQUE,
teacher_id  INTEGER NOT NULL,

CONSTRAINT fk_subject_teacher FOREIGN KEY (teacher_id ) REFERENCES teachers (user_id)
);


CREATE TABLE IF NOT EXISTS students(
user_id INTEGER PRIMARY KEY,
group_id INTEGER NOT NULL,

CONSTRAINT fk_student_group FOREIGN KEY (group_id) REFERENCES academic_groups (group_id),
CONSTRAINT fk_student_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS groups_subjects(
group_id INTEGER NOT NULL,
subject_id INTEGER NOT NULL,

CONSTRAINT pk_groups_subjects PRIMARY KEY (group_id, subject_id),
CONSTRAINT fk_groups_subjects_group FOREIGN KEY (group_id) REFERENCES academic_groups(group_id),
CONSTRAINT fk_groups_subjects_subject FOREIGN KEY (subject_id) REFERENCES subjects(subject_id)
);