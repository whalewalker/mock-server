    ALTER TABLE IF EXISTS permissions
        ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE;

    ALTER TABLE IF EXISTS roles
        ADD COLUMN deleted BOOLEAN NOT NULL DEFAULT FALSE;

-- Update existing entries to set deleted to false and update created_at and updated_at to current timestamp if null
UPDATE permissions SET deleted = FALSE;
UPDATE refresh_tokens SET deleted = FALSE;
UPDATE roles SET deleted = FALSE;
UPDATE users SET deleted = FALSE;

UPDATE users SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
UPDATE users SET updated_at = CURRENT_TIMESTAMP WHERE updated_at IS NULL;
UPDATE refresh_tokens SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
UPDATE refresh_tokens SET updated_at = CURRENT_TIMESTAMP WHERE updated_at IS NULL;

UPDATE roles SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
UPDATE roles SET updated_at = CURRENT_TIMESTAMP WHERE updated_at IS NULL;
UPDATE permissions SET created_at = CURRENT_TIMESTAMP WHERE created_at IS NULL;
UPDATE permissions SET updated_at = CURRENT_TIMESTAMP WHERE updated_at IS NULL;