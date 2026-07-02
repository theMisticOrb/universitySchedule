ALTER TABLE subjects DROP CONSTRAINT fk_subject_teacher;

ALTER TABLE subjects ADD CONSTRAINT fk_subject_teacher 
FOREIGN KEY (teacher_id) REFERENCES teachers (user_id) ON DELETE CASCADE;

ALTER TABLE lessons DROP CONSTRAINT fk_lesson_teacher;
ALTER TABLE lessons ADD CONSTRAINT fk_lesson_teacher
FOREIGN KEY (teacher_id) REFERENCES teachers (user_id) ON DELETE CASCADE;

ALTER TABLE lessons DROP CONSTRAINT fk_lesson_subject;
ALTER TABLE lessons ADD CONSTRAINT fk_lesson_subject
FOREIGN KEY (subject_id) REFERENCES subjects (subject_id) ON DELETE CASCADE;


CREATE TABLE IF NOT EXISTS activation_tokens (
id BIGSERIAL PRIMARY KEY,
token VARCHAR(255) NOT NULL UNIQUE,
user_id INTEGER NOT NULL,
expiry_date TIMESTAMP NOT NULL,

CONSTRAINT fk_reset_token_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE);