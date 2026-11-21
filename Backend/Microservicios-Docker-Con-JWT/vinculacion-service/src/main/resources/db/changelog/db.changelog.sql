--liquibase formatted sql

--changeset corpaliv:vinculacion-001
CREATE TABLE tipo_colaboracion (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 codigo VARCHAR(40) NOT NULL UNIQUE,
 nombre VARCHAR(100) NOT NULL,
 descripcion TEXT,
 activo BIT(1) NOT NULL DEFAULT b'1'
);

CREATE TABLE solicitud_colaboracion (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 nombre_contacto VARCHAR(120) NOT NULL,
 organizacion VARCHAR(150),
 email VARCHAR(150),
 telefono VARCHAR(40),
 tipo_colaboracion_id BIGINT NOT NULL,
 programa_id BIGINT,
 producto_servicio_id BIGINT,
 mensaje TEXT,
 acepta_privacidad BIT(1) NOT NULL,
 estado VARCHAR(30) NOT NULL,
 fecha_creacion DATETIME NOT NULL,
 fecha_actualizacion DATETIME NOT NULL,
 CONSTRAINT fk_solicitud_tipo FOREIGN KEY (tipo_colaboracion_id) REFERENCES tipo_colaboracion(id)
);

CREATE TABLE historial_solicitud (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 solicitud_id BIGINT NOT NULL,
 estado_anterior VARCHAR(30),
 estado_nuevo VARCHAR(30) NOT NULL,
 observacion_interna TEXT,
 usuario_gestor VARCHAR(120),
 fecha_cambio DATETIME NOT NULL,
 CONSTRAINT fk_historial_solicitud FOREIGN KEY (solicitud_id) REFERENCES solicitud_colaboracion(id)
);

--changeset corpaliv:vinculacion-002
INSERT INTO tipo_colaboracion (codigo, nombre, descripcion, activo) VALUES
('DONACION', 'Donaciones', 'Interes en realizar aportes.', b'1'),
('ALIANZA_RSE', 'Alianzas con empresas', 'Interes en establecer una alianza de responsabilidad social.', b'1'),
('VOLUNTARIADO', 'Voluntariado', 'Interes en participar como voluntario.', b'1'),
('COMPRA', 'Productos y servicios', 'Interes en consultar productos o servicios.', b'1'),
('CAPACITACION', 'Capacitaciones', 'Interes en actividades formativas.', b'1'),
('OTRO', 'Otro', 'Otra modalidad de colaboracion.', b'1');
