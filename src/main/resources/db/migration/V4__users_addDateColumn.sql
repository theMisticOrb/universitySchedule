ALTER TABLE users ADD COLUMN created_at TIMESTAMP;

ALTER TABLE activation_tokens ADD COLUMN used_at TIMESTAMP;


CREATE TABLE IF NOT EXISTS teacher_subjects (
    teacher_id INTEGER NOT NULL,
    subject_id INTEGER NOT NULL,
    PRIMARY KEY (teacher_id, subject_id),
    CONSTRAINT fk_teacher_subjects_teacher 
        FOREIGN KEY (teacher_id) REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_teacher_subjects_subject 
        FOREIGN KEY (subject_id) REFERENCES subjects(subject_id) ON DELETE CASCADE
);

INSERT INTO teacher_subjects (teacher_id, subject_id)
SELECT teacher_id, subject_id 
FROM subjects 
WHERE teacher_id IS NOT NULL;

ALTER TABLE subjects DROP COLUMN teacher_id;