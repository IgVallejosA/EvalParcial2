CREATE TABLE patrocinadores (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_empresa  VARCHAR(100) NOT NULL,
    industria       VARCHAR(50)  NOT NULL,
    pais_origen     VARCHAR(50)  NOT NULL,
    sitio_web       VARCHAR(200) NULL,
    tier            VARCHAR(20)  NOT NULL,
    activo          BIT          NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_patrocinadores_nombre UNIQUE (nombre_empresa)
) ENGINE=InnoDB;

CREATE TABLE contratos (
    id                BIGINT       NOT NULL AUTO_INCREMENT,
    id_equipo         BIGINT       NOT NULL,
    nombre_equipo     VARCHAR(80)  NULL,
    monto_anual_usd   DOUBLE       NOT NULL,
    fecha_inicio      DATE         NOT NULL,
    fecha_fin         DATE         NOT NULL,
    tipo_acuerdo      VARCHAR(30)  NULL,
    activo            BIT          NOT NULL,
    patrocinador_id   BIGINT       NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_contratos_patrocinador FOREIGN KEY (patrocinador_id) REFERENCES patrocinadores (id)
) ENGINE=InnoDB;
