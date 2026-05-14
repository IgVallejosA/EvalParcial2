CREATE TABLE torneos (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    nombre            VARCHAR(150) NOT NULL,
    id_juego          BIGINT       NOT NULL,
    nombre_juego      VARCHAR(100) NULL,
    organizador       VARCHAR(100) NOT NULL,
    fecha_inicio      DATE         NOT NULL,
    fecha_fin         DATE         NOT NULL,
    premio_total_usd  DOUBLE       NOT NULL,
    max_equipos       INT          NOT NULL,
    modalidad         VARCHAR(30)  NOT NULL,
    estado            VARCHAR(30)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE fases_torneo (
    id            BIGINT      NOT NULL AUTO_INCREMENT,
    nombre_fase   VARCHAR(50) NOT NULL,
    orden         INT         NOT NULL,
    fecha_inicio  DATE        NOT NULL,
    fecha_fin     DATE        NOT NULL,
    formato       VARCHAR(30) NULL,
    torneo_id     BIGINT      NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_fases_torneo_torneo FOREIGN KEY (torneo_id) REFERENCES torneos (id)
) ENGINE=InnoDB;
