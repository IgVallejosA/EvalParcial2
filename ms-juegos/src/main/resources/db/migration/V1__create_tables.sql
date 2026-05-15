CREATE TABLE juegos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    genero VARCHAR(20) NOT NULL,
    desarrolladora VARCHAR(100) NOT NULL,
    fecha_lanzamiento DATE NOT NULL,
    plataforma VARCHAR(100) NOT NULL,
    prize_pool_total_usd DOUBLE,
    activo BOOLEAN NOT NULL
);

CREATE TABLE modos_competitivos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre_modo VARCHAR(80) NOT NULL,
    max_jugadores_por_equipo INT NOT NULL,
    duracion_promedio_minutos INT,
    activo BOOLEAN NOT NULL,
    juego_id BIGINT NOT NULL,

    CONSTRAINT fk_modo_juego
        FOREIGN KEY (juego_id)
        REFERENCES juegos(id)
        ON DELETE CASCADE
);