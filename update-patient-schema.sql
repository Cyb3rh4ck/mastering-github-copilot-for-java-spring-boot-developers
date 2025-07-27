-- Update patient table to match new schema
-- Remove columns that are not in the new schema
ALTER TABLE patient 
DROP COLUMN IF EXISTS email,
DROP COLUMN IF EXISTS phone,
DROP COLUMN IF EXISTS date_of_birth,
DROP COLUMN IF EXISTS address;

-- Add age column if it doesn't exist
ALTER TABLE patient 
ADD COLUMN IF NOT EXISTS age INT;

-- Verify the final schema matches the requirement
-- TABLE patient (
--     id INT NOT NULL AUTO_INCREMENT,
--     last_name VARCHAR(255) NOT NULL,
--     first_name VARCHAR(255) NOT NULL,
--     age INT,
--     PRIMARY KEY (id)
-- )
