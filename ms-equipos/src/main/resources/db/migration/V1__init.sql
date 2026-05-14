CREATE TABLE equipos (
    id                     BIGINT       NOT NULL AUTO_INCREMENT,
    nombre                 VARCHAR(80)  NOT NULL,
    region                 VARCHAR(20)  NOT NULL,
    fecha_fundacion        DATE         NOT NULL,
    ranking_mundial        INT          NULL,
    presupuesto_anual_usd  DOUBLE       NULL,
    activo                 BIT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_equipos_nombre_region UNIQUE (nombre, region)
) ENGINE=InnoDB;

CREATE TABLE staff_tecnico (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    nombre               VARCHAR(100) NOT NULL,
    rol_staff            VARCHAR(30)  NOT NULL,
    salario_mensual_usd  DOUBLE       NULL,
    activo               BIT          NOT NULL,
    equipo_id            BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_staff_tecnico_equipo FOREIGN KEY (equipo_id) REFERENCES equipos (id)
) ENGINE=InnoDB;

CREATE TABLE roster_historico (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    id_jugador           BIGINT       NOT NULL,
    nickname_al_unirse   VARCHAR(50)  NULL,
    fecha_inicio         DATE         NOT NULL,
    fecha_fin            DATE         NULL,
    equipo_id            BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_roster_historico_equipo FOREIGN KEY (equipo_id) REFERENCES equipos (id)
) ENGINE=InnoDB;
