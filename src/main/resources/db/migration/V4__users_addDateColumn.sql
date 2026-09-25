ALTER TABLE users
ADD COLUMN is_registration_completed BOOLEAN DEFAULT false,
ADD COLUMN created_at TIMESTAMP;

CREATE TABLE IF NOT EXISTS activation_tokens (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id INTEGER NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    used_at TIMESTAMP,

    CONSTRAINT fk_activation_token_user FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS teacher_subjects (
    teacher_id INTEGER NOT NULL,
    subject_id INTEGER NOT NULL,
    PRIMARY KEY (teacher_id, subject_id),
    CONSTRAINT fk_teacher_subjects_teacher FOREIGN KEY (teacher_id) REFERENCES teachers(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_teacher_subjects_subject FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE
);

INSERT INTO teacher_subjects (teacher_id, subject_id)
SELECT teacher_id, subject_id 
FROM subjects 
WHERE teacher_id IS NOT NULL;

ALTER TABLE subjects DROP COLUMN teacher_id;