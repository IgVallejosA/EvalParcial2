CREATE TABLE streamers (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    nombre_artistico VARCHAR(80) NOT NULL UNIQUE,
    nombre_real VARCHAR(100),

    pais VARCHAR(50) NOT NULL,
    idioma VARCHAR(30) NOT NULL,

    rol VARCHAR(30) NOT NULL,
    plataforma_principal VARCHAR(30) NOT NULL,

    seguidores INT,
    activo BOOLEAN NOT NULL
);

CREATE TABLE asignaciones_cobertura (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    id_torneo BIGINT NOT NULL,
    nombre_torneo VARCHAR(150),

    fecha_inicio_cobertura DATE NOT NULL,
    fecha_fin_cobertura DATE NOT NULL,

    honorarios_usd DOUBLE,

    streamer_id BIGINT NOT NULL,

    CONSTRAINT fk_asignacion_streamer
        FOREIGN KEY (streamer_id)
        REFERENCES streamers(id)
        ON DELETE CASCADE
);