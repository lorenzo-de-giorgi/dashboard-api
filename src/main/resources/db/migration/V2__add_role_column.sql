-- Aggiunge la colonna `role` agli utenti e imposta un valore di default
ALTER TABLE users ADD COLUMN role VARCHAR(50);
UPDATE users SET role = 'ROLE_USER' WHERE role IS NULL;
ALTER TABLE users ALTER COLUMN role SET NOT NULL;
