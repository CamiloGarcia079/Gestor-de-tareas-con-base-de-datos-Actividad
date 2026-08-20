-- =========================================================
--  GESTOR DE TAREAS - Script de creacion de base de datos
--  Motor: MySQL 8.x
--  Este script NO es el codigo de drawSQL (ese ya se genero
--  aparte). Este es el script que usa la aplicacion Java
--  (JDBC) para crear y poblar el esquema real en MySQL.
-- =========================================================

CREATE DATABASE IF NOT EXISTS gestor_tareas
    CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE gestor_tareas;

-- ---------------------------------------------------------
-- Catalogo: tipo de persona (Scrum Master, Product Owner, Developer)
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS type_person (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50)  NOT NULL UNIQUE,
    descripcion VARCHAR(200) NULL
);

-- ---------------------------------------------------------
-- Catalogo: estado de la tarea (Por hacer, En proceso, Finalizada)
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS status_task (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50)  NOT NULL UNIQUE,
    descripcion VARCHAR(200) NULL
);

-- ---------------------------------------------------------
-- Equipo de trabajo
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS team (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NULL
);

-- ---------------------------------------------------------
-- Persona (integrante) - pertenece a un tipo y a un equipo
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS person (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    nombre           VARCHAR(100) NOT NULL,
    email            VARCHAR(150) NOT NULL UNIQUE,
    id_tipo_persona  INT NOT NULL,
    id_equipo        INT NULL,
    CONSTRAINT fk_person_tipo   FOREIGN KEY (id_tipo_persona) REFERENCES type_person(id),
    CONSTRAINT fk_person_equipo FOREIGN KEY (id_equipo)        REFERENCES team(id)
);

-- ---------------------------------------------------------
-- Historico de vinculacion persona <-> equipo (relacion N:M)
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS team_person (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    id_team    INT NOT NULL,
    id_person  INT NOT NULL,
    fecha_alta DATE NULL,
    CONSTRAINT fk_tp_team   FOREIGN KEY (id_team)   REFERENCES team(id),
    CONSTRAINT fk_tp_person FOREIGN KEY (id_person) REFERENCES person(id)
);

-- ---------------------------------------------------------
-- Tarea
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS task (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    titulo         VARCHAR(150) NOT NULL,
    descripcion    TEXT NULL,
    prioridad      VARCHAR(10)  NOT NULL,   -- Alta / Media / Baja
    id_estado      INT NOT NULL,
    fecha_creacion DATE NOT NULL,
    fecha_limite   DATE NULL,
    id_equipo      INT NOT NULL,
    CONSTRAINT fk_task_estado FOREIGN KEY (id_estado) REFERENCES status_task(id),
    CONSTRAINT fk_task_equipo FOREIGN KEY (id_equipo) REFERENCES team(id)
);

-- ---------------------------------------------------------
-- Asignacion de una tarea a una persona (relacion N:M)
-- ---------------------------------------------------------
CREATE TABLE IF NOT EXISTS assement_task (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    id_task   INT NOT NULL,
    id_person INT NOT NULL,
    nota      VARCHAR(255) NULL,
    CONSTRAINT fk_at_task   FOREIGN KEY (id_task)   REFERENCES task(id),
    CONSTRAINT fk_at_person FOREIGN KEY (id_person) REFERENCES person(id)
);

-- ---------------------------------------------------------
-- Datos iniciales (catalogos)
-- ---------------------------------------------------------
INSERT INTO type_person (nombre, descripcion) VALUES
    ('Scrum Master', 'Facilita el proceso agil del equipo'),
    ('Product Owner', 'Define y prioriza el producto'),
    ('Developer', 'Construye los incrementos del producto');

INSERT INTO status_task (nombre, descripcion) VALUES
    ('Por hacer',  'Tarea creada, aun no iniciada'),
    ('En proceso', 'Tarea que se esta trabajando actualmente'),
    ('Finalizada', 'Tarea completada');
