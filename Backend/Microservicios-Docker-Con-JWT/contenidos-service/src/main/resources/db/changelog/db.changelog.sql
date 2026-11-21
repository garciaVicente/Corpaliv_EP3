--liquibase formatted sql

--changeset corpaliv:contenidos-001
CREATE TABLE categoria (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(100) NOT NULL,
 descripcion TEXT,
 tipo VARCHAR(40)
);

CREATE TABLE programa (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(120) NOT NULL,
 resumen VARCHAR(255),
 descripcion TEXT,
 categoria VARCHAR(100),
 imagen_url VARCHAR(500),
 publicado BIT(1) NOT NULL DEFAULT b'1',
 fecha_creacion DATETIME,
 fecha_actualizacion DATETIME
);

CREATE TABLE historia_impacto (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 titulo VARCHAR(150) NOT NULL,
 resumen VARCHAR(255),
 contenido TEXT,
 imagen_url VARCHAR(500),
 autorizada BIT(1) NOT NULL DEFAULT b'0',
 anonimizada BIT(1) NOT NULL DEFAULT b'0',
 publicada BIT(1) NOT NULL DEFAULT b'0',
 fecha_publicacion DATE
);

CREATE TABLE indicador_impacto (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 nombre VARCHAR(120) NOT NULL,
 descripcion TEXT,
 valor DECIMAL(12,2),
 unidad VARCHAR(60),
 periodo VARCHAR(60),
 fuente VARCHAR(255),
 fecha_actualizacion DATETIME,
 publicado BIT(1) NOT NULL DEFAULT b'0'
);

CREATE TABLE producto_servicio (
 id BIGINT AUTO_INCREMENT PRIMARY KEY,
 categoria_id BIGINT,
 nombre VARCHAR(150) NOT NULL,
 descripcion TEXT,
 imagen_url VARCHAR(500),
 modalidad_consulta VARCHAR(100),
 disponible BIT(1) NOT NULL DEFAULT b'1',
 publicado BIT(1) NOT NULL DEFAULT b'0',
 CONSTRAINT fk_producto_categoria FOREIGN KEY (categoria_id) REFERENCES categoria(id)
);

--changeset corpaliv:contenidos-002
INSERT INTO categoria (nombre, descripcion, tipo) VALUES
('Programas', 'Categoria de programas institucionales. Contenido sintetico de prueba.', 'PROGRAMA'),
('Productos y servicios', 'Catalogo ilustrativo pendiente de validacion institucional.', 'PRODUCTO');

INSERT INTO programa (nombre, resumen, descripcion, categoria, publicado) VALUES
('Formacion sociolaboral', 'Contenido de prueba para el MVP.', 'Ejemplo de contenido que debe reemplazarse por informacion institucional autorizada.', 'Inclusión', TRUE),
('Vinculacion con empresas', 'Contenido de prueba para el MVP.', 'Ejemplo de programa para mostrar como se consulta la informacion desde el backend.', 'Alianzas', TRUE);

INSERT INTO historia_impacto (titulo, resumen, contenido, autorizada, anonimizada, publicada, fecha_publicacion) VALUES
('Formacion para la autonomia', 'Historia ilustrativa de prueba.', 'Contenido sintetico. Reemplazar solo por una historia autorizada y anonimizada.', FALSE, FALSE, FALSE, CURRENT_DATE);

INSERT INTO indicador_impacto (nombre, descripcion, valor, unidad, periodo, fuente, fecha_actualizacion, publicado) VALUES
('Indicador de ejemplo', 'Dato sintetico de prueba; no representa resultados oficiales.', 0, 'valor de prueba', 'periodo de prueba', 'Dato ficticio para verificar la API', NOW(), FALSE);

INSERT INTO producto_servicio (categoria_id, nombre, descripcion, modalidad_consulta, disponible, publicado) VALUES
(2, 'Producto de ejemplo', 'Ficha ilustrativa; consultar disponibilidad real con CORPALIV.', 'Consulta por formulario', TRUE, TRUE);
