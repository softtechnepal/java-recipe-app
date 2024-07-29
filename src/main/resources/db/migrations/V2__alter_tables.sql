-- Step 1: Add the status column
-- TODO: Prachan active status is added on the table.
--  Please check it during login of the user.
--  log in if user is active and show popup user is disabled and
--  contact the administrator
ALTER TABLE Users
    ADD COLUMN status VARCHAR(10) DEFAULT 'active';

-- Step 2: Add a CHECK constraint to enforce only 'active' or 'disabled' values
ALTER TABLE Users
    ADD CONSTRAINT ck_users_status CHECK (status IN ('active', 'disabled'));
