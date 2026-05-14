CREATE TABLE partidas (
    id                       BIGINT       NOT NULL AUTO_INCREMENT,
    id_torneo                BIGINT       NOT NULL,
    nombre_torneo            VARCHAR(150) NULL,
    id_equipo_local          BIGINT       NOT NULL,
    nombre_equipo_local      VARCHAR(80)  NULL,
    id_equipo_visitante      BIGINT       NOT NULL,
    nombre_equipo_visitante  VARCHAR(80)  NULL,
    fecha_hora               DATETIME(6)  NOT NULL,
    duracion_minutos         INT          NULL,
    marcador_local           INT          NULL,
    marcador_visitante       INT          NULL,
    id_equipo_ganador        BIGINT       NULL,
    estado                   VARCHAR(30)  NOT NULL,
    mapa_o_escenario         VARCHAR(80)  NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;
