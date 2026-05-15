CREATE TABLE estadisticas (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    id_jugador BIGINT NOT NULL,
    nickname_jugador VARCHAR(50),

    id_partida BIGINT NOT NULL,

    kills INT NOT NULL,
    deaths INT NOT NULL,
    assists INT NOT NULL,

    kda DOUBLE,

    damage_dealt INT,

    tiempo_jugado_minutos INT,

    mvp BOOLEAN NOT NULL,

    fecha_registro DATETIME NOT NULL,

    CONSTRAINT uk_jugador_partida
        UNIQUE (id_jugador, id_partida)
);