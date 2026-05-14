CREATE TABLE streamers (
    id                    BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_artistico      VARCHAR(80)  NOT NULL,
    nombre_real           VARCHAR(100) NULL,
    pais                  VARCHAR(50)  NOT NULL,
    idioma                VARCHAR(30)  NOT NULL,
    rol                   VARCHAR(30)  NOT NULL,
    plataforma_principal  VARCHAR(30)  NOT NULL,
    seguidores            INT          NULL,
    activo                BIT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_streamers_nombre_artistico UNIQUE (nombre_artistico)
) ENGINE=InnoDB;

CREATE TABLE asignaciones_cobertura (
    id                      BIGINT       NOT NULL AUTO_INCREMENT,
    id_torneo               BIGINT       NOT NULL,
    nombre_torneo           VARCHAR(150) NULL,
    fecha_inicio_cobertura  DATE         NOT NULL,
    fecha_fin_cobertura     DATE         NOT NULL,
    honorarios_usd          DOUBLE       NULL,
    streamer_id             BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_asignaciones_cobertura_streamer FOREIGN KEY (streamer_id) REFERENCES streamers (id)
) ENGINE=InnoDB;
