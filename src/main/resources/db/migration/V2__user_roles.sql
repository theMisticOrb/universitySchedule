ALTER TABLE users
ADD COLUMN user_email VARCHAR(225),
ADD COLUMN user_password VARCHAR(225);

CREATE TABLE IF NOT EXISTS user_roles(
user_id INTEGER NOT NULL,
user_role varchar(50) NOT NULL,

PRIMARY KEY(user_id, user_role),
CONSTRAINT fk_user_roles_user FOREIGN KEY (user_id) REFERENCES users (user_id) ON DELETE CASCADE
);

INSERT INTO user_roles (user_id, user_role)
SELECT user_id, user_role FROM users;

ALTER TABLE users DROP CONSTRAINT IF EXISTS chk_user_role;

ALTER TABLE users DROP COLUMN user_role;

ALTER TABLE users
ALTER COLUMN user_email SET NOT NULL,
ALTER COLUMN user_password SET NOT NULL;

ALTER TABLE users
ADD CONSTRAINT uk_user_email UNIQUE (user_email);