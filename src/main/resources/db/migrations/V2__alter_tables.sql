-- Step 1: Add the status column
ALTER TABLE Users
    ADD COLUMN status VARCHAR(10) DEFAULT 'ACTIVE';

-- Step 2: Add a CHECK constraint to enforce only 'active' or 'disabled' values
ALTER TABLE Users
    ADD CONSTRAINT ck_users_status CHECK (status IN ('ACTIVE', 'PENDING', 'DISABLED'));

-- DROP TABLE allergywarnings;

-- DROP TABLE RecipeAllergyWarnings;

ALTER TABLE recipes
    ADD COLUMN warnings TEXT DEFAULT '';

CREATE SEQUENCE IF NOT EXISTS step_id_seq START 100 INCREMENT BY 1;

CREATE TABLE RecipeSteps
(
    step_id          BIGINT DEFAULT NEXTVAL('step_id_seq') NOT NULL,
    step_order       INT                                   NOT NULL,
    step_name        VARCHAR(255)                          NOT NULL,
    step_description TEXT,
    recipe_id        INT,
    CONSTRAINT step_id PRIMARY KEY (step_id),
    FOREIGN KEY (recipe_id) REFERENCES recipes (recipe_id)
);

ALTER TABLE users
    ADD COLUMN gender VARCHAR(10) DEFAULT NULL;

ALTER TABLE users
    ADD COLUMN dob DATE DEFAULT NULL;

ALTER TABLE users
    ADD CONSTRAINT ck_users_gender CHECK (gender IN ('MALE', 'FEMALE', 'OTHER'));

ALTER TABLE users
    ADD CONSTRAINT ck_users_dob CHECK (dob <= CURRENT_DATE);

ALTER TABLE reviews
    ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    ADD COLUMN updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

ALTER TABLE users
    ADD COLUMN profile_picture TEXT DEFAULT NULL;

ALTER TABLE recipes
    ADD COLUMN number_of_servings INT DEFAULT 0,
    ADD COLUMN total_preparation_time INT DEFAULT 0;