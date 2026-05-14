CREATE TABLE juegos (
    id                    BIGINT       NOT NULL AUTO_INCREMENT,
    nombre                VARCHAR(100) NOT NULL,
    genero                VARCHAR(20)  NOT NULL,
    desarrolladora        VARCHAR(100) NOT NULL,
    fecha_lanzamiento     DATE         NOT NULL,
    plataforma            VARCHAR(100) NOT NULL,
    prize_pool_total_usd  DOUBLE       NULL,
    activo                BIT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_juegos_nombre UNIQUE (nombre)
) ENGINE=InnoDB;

CREATE TABLE modos_competitivos (
    id                          BIGINT      NOT NULL AUTO_INCREMENT,
    nombre_modo                 VARCHAR(80) NOT NULL,
    max_jugadores_por_equipo    INT         NOT NULL,
    duracion_promedio_minutos   INT         NULL,
    activo                      BIT         NOT NULL,
    juego_id                    BIGINT      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_modos_competitivos_juego FOREIGN KEY (juego_id) REFERENCES juegos (id)
) ENGINE=InnoDB;
