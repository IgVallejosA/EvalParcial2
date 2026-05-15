-- =============================================
-- TABLA: torneos
-- =============================================
CREATE TABLE torneos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    nombre VARCHAR(150) NOT NULL,
    id_juego BIGINT NOT NULL,
    nombre_juego VARCHAR(100),

    organizador VARCHAR(100) NOT NULL,

    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,

    premio_total_usd DOUBLE NOT NULL,

    max_equipos INT NOT NULL,

    modalidad VARCHAR(30) NOT NULL,
    estado VARCHAR(30) NOT NULL,

    CONSTRAINT uk_torneo_nombre UNIQUE (nombre)
);

-- =============================================
-- TABLA: fases_torneo
-- =============================================
CREATE TABLE fases_torneo (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    nombre_fase VARCHAR(50) NOT NULL,

    orden INT NOT NULL,

    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,

    formato VARCHAR(30),

    torneo_id BIGINT NOT NULL,

    CONSTRAINT fk_fase_torneo
        FOREIGN KEY (torneo_id)
        REFERENCES torneos(id)
        ON DELETE CASCADE,

    CONSTRAINT uk_fase_orden
        UNIQUE (torneo_id, orden)
);

-- =============================================
-- ÍNDICES
-- =============================================
CREATE INDEX idx_torneos_estado
    ON torneos(estado);

CREATE INDEX idx_torneos_id_juego
    ON torneos(id_juego);

CREATE INDEX idx_fases_torneo_torneo
    ON fases_torneo(torneo_id);