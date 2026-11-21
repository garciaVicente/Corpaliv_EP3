CREATE DATABASE IF NOT EXISTS bd_corpaliv_auth CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_corpaliv_contenidos CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE DATABASE IF NOT EXISTS bd_corpaliv_vinculacion CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

GRANT ALL PRIVILEGES ON bd_corpaliv_auth.* TO 'corpaliv'@'%';
GRANT ALL PRIVILEGES ON bd_corpaliv_contenidos.* TO 'corpaliv'@'%';
GRANT ALL PRIVILEGES ON bd_corpaliv_vinculacion.* TO 'corpaliv'@'%';
FLUSH PRIVILEGES;
