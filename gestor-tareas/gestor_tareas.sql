-- ============================================================
--  GESTOR DE TAREAS SCRUM
--  Script SQL — ejecutar en MySQL Workbench o draw.io (SQL)
--  Base de datos: gestor_tareas
-- ============================================================

CREATE DATABASE IF NOT EXISTS gestor_tareas
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;

USE gestor_tareas;

-- ── 1. TYPE_PERSON ────────────────────────────────────────────
-- Catálogo de roles Scrum (Scrum Master, Product Owner, Developer)
-- Normalizado como ENUM directamente en la tabla person para simplicidad.
-- Se mantiene la tabla para cumplir con el modelo E-R solicitado.
CREATE TABLE IF NOT EXISTS type_person (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50)  NOT NULL UNIQUE,   -- Scrum Master | Product Owner | Developer
    descripcion VARCHAR(200) NULL
);

INSERT INTO type_person (nombre, descripcion) VALUES
    ('SCRUM_MASTER',   'Facilita el proceso Scrum y elimina impedimentos'),
    ('PRODUCT_OWNER',  'Define y prioriza el backlog del producto'),
    ('DEVELOPER',      'Desarrolla e implementa las tareas del sprint')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

-- ── 2. TEAM ───────────────────────────────────────────────────
-- Equipo de trabajo Scrum
CREATE TABLE IF NOT EXISTS team (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255) NULL
);

-- ── 3. PERSON ─────────────────────────────────────────────────
-- Persona miembro de un equipo con un rol (type_person)
CREATE TABLE IF NOT EXISTS person (
    id           INT AUTO_INCREMENT PRIMARY KEY,
    nombre       VARCHAR(100) NOT NULL,
    email        VARCHAR(150) NOT NULL UNIQUE,
    tipo_persona ENUM('SCRUM_MASTER','PRODUCT_OWNER','DEVELOPER') NOT NULL,
    id_equipo    INT          NOT NULL,
    CONSTRAINT fk_person_team
        FOREIGN KEY (id_equipo) REFERENCES team(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ── 4. TEAM_PERSON (tabla pivote — relación N:M team ↔ person) ─
-- Permite que una persona pertenezca a varios equipos simultáneamente
CREATE TABLE IF NOT EXISTS team_person (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    id_team    INT NOT NULL,
    id_person  INT NOT NULL,
    fecha_alta DATE NULL,
    CONSTRAINT fk_tp_team   FOREIGN KEY (id_team)   REFERENCES team(id)   ON DELETE CASCADE,
    CONSTRAINT fk_tp_person FOREIGN KEY (id_person) REFERENCES person(id) ON DELETE CASCADE,
    CONSTRAINT uq_team_person UNIQUE (id_team, id_person)
);

-- ── 5. STATUS_TASK ────────────────────────────────────────────
-- Catálogo de estados del ciclo de vida de una tarea
CREATE TABLE IF NOT EXISTS status_task (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50)  NOT NULL UNIQUE,
    descripcion VARCHAR(200) NULL
);

INSERT INTO status_task (nombre, descripcion) VALUES
    ('POR_HACER',   'La tarea está pendiente de iniciar'),
    ('EN_PROCESO',  'La tarea está siendo trabajada'),
    ('EN_REVISION', 'La tarea espera revisión o QA'),
    ('FINALIZADO',  'La tarea ha sido completada')
ON DUPLICATE KEY UPDATE descripcion = VALUES(descripcion);

-- ── 6. TASK ───────────────────────────────────────────────────
-- Tarea Scrum con prioridad, estado y fecha límite
CREATE TABLE IF NOT EXISTS task (
    id              INT AUTO_INCREMENT PRIMARY KEY,
    titulo          VARCHAR(150) NOT NULL,
    descripcion     TEXT         NULL,
    prioridad       ENUM('ALTA','MEDIA','BAJA') NOT NULL DEFAULT 'MEDIA',
    estado          ENUM('POR_HACER','EN_PROCESO','EN_REVISION','FINALIZADO')
                    NOT NULL DEFAULT 'POR_HACER',
    fecha_creacion  DATE         NOT NULL DEFAULT (CURRENT_DATE),
    fecha_limite    DATE         NULL,
    id_equipo       INT          NOT NULL,
    CONSTRAINT fk_task_team
        FOREIGN KEY (id_equipo) REFERENCES team(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- ── 7. ASSEMENT_TASK (asignación tarea ↔ persona) ────────────
-- Tabla pivote N:M entre task y person
CREATE TABLE IF NOT EXISTS assement_task (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    id_task   INT          NOT NULL,
    id_person INT          NOT NULL,
    nota      VARCHAR(255) NULL,
    CONSTRAINT fk_at_task   FOREIGN KEY (id_task)   REFERENCES task(id)   ON DELETE CASCADE,
    CONSTRAINT fk_at_person FOREIGN KEY (id_person) REFERENCES person(id) ON DELETE CASCADE,
    CONSTRAINT uq_assement  UNIQUE (id_task, id_person)
);

-- ============================================================
--  DATOS DE PRUEBA
-- ============================================================

-- Equipos
INSERT INTO team (nombre, descripcion) VALUES
    ('Team Alpha', 'Equipo de desarrollo backend'),
    ('Team Beta',  'Equipo de desarrollo frontend');

-- Personas
INSERT INTO person (nombre, email, tipo_persona, id_equipo) VALUES
    ('Carlos García',  'carlos@scrum.com',   'SCRUM_MASTER',  1),
    ('Ana Martínez',   'ana@scrum.com',      'PRODUCT_OWNER', 1),
    ('Luis Rodríguez', 'luis@scrum.com',     'DEVELOPER',     1),
    ('María López',    'maria@scrum.com',    'DEVELOPER',     1),
    ('Felipe Sánchez', 'felipe@scrum.com',   'SCRUM_MASTER',  2),
    ('Camilo Gómez',   'camilo@scrum.com',   'DEVELOPER',     2);

-- Tareas
INSERT INTO task (titulo, descripcion, prioridad, estado, fecha_creacion, fecha_limite, id_equipo) VALUES
    ('Diseñar base de datos',   'Modelo E-R y normalización',            'ALTA',  'FINALIZADO',  CURDATE(), DATE_ADD(CURDATE(), INTERVAL 7  DAY), 1),
    ('Implementar login',       'Autenticación de usuarios con JWT',     'ALTA',  'EN_PROCESO',  CURDATE(), DATE_ADD(CURDATE(), INTERVAL 5  DAY), 1),
    ('Crear API REST',          'Endpoints CRUD para tareas',            'MEDIA', 'POR_HACER',   CURDATE(), DATE_ADD(CURDATE(), INTERVAL 14 DAY), 1),
    ('Diseñar wireframes',      'Pantallas principales del sistema',     'MEDIA', 'EN_REVISION', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 3  DAY), 2),
    ('Documentar proyecto',     'README y diagramas UML',                'BAJA',  'POR_HACER',   CURDATE(), DATE_ADD(CURDATE(), INTERVAL 20 DAY), 2);

-- Asignaciones tarea ↔ persona
INSERT INTO assement_task (id_task, id_person, nota) VALUES
    (1, 1, 'Supervisión general'),
    (1, 3, 'Implementación del modelo'),
    (2, 3, 'Desarrollo del módulo de auth'),
    (2, 4, 'Apoyo en implementación'),
    (3, 4, 'Desarrollo de endpoints'),
    (4, 6, 'Diseño UI/UX'),
    (5, 5, 'Documentación técnica');

-- Vinculaciones adicionales en team_person
INSERT INTO team_person (id_team, id_person, fecha_alta) VALUES
    (1, 1, CURDATE()), (1, 2, CURDATE()), (1, 3, CURDATE()), (1, 4, CURDATE()),
    (2, 5, CURDATE()), (2, 6, CURDATE());
