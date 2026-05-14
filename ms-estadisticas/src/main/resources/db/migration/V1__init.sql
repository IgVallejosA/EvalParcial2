CREATE TABLE estadisticas (
    id                     BIGINT       NOT NULL AUTO_INCREMENT,
    id_jugador             BIGINT       NOT NULL,
    nickname_jugador       VARCHAR(50)  NULL,
    id_partida             BIGINT       NOT NULL,
    kills                  INT          NOT NULL,
    deaths                 INT          NOT NULL,
    assists                INT          NOT NULL,
    kda                    DOUBLE       NULL,
    damage_dealt           INT          NULL,
    tiempo_jugado_minutos  INT          NULL,
    mvp                    BIT          NOT NULL,
    fecha_registro         DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_estadisticas_jugador_partida UNIQUE (id_jugador, id_partida)
) ENGINE=InnoDB;
