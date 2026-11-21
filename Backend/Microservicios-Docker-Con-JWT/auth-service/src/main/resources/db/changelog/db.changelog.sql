--liquibase formatted sql

--changeset corpaliv:auth-001
CREATE TABLE usuario_gestor (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(150) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    rol VARCHAR(30) NOT NULL,
    activo BIT(1) NOT NULL DEFAULT b'1'
);
