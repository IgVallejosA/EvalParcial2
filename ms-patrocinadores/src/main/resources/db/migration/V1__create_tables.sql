CREATE TABLE patrocinadores (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre_empresa VARCHAR(100) NOT NULL UNIQUE,
    industria VARCHAR(50) NOT NULL,
    pais_origen VARCHAR(50) NOT NULL,
    sitio_web VARCHAR(200),
    tier VARCHAR(20) NOT NULL,
    activo BOOLEAN NOT NULL
);

CREATE TABLE contratos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_equipo BIGINT NOT NULL,
    nombre_equipo VARCHAR(80),
    monto_anual_usd DOUBLE NOT NULL,
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE NOT NULL,
    tipo_acuerdo VARCHAR(30),
    activo BOOLEAN NOT NULL,

    patrocinador_id BIGINT NOT NULL,

    CONSTRAINT fk_contrato_patrocinador
        FOREIGN KEY (patrocinador_id)
        REFERENCES patrocinadores(id)
        ON DELETE CASCADE
);