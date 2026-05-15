CREATE TABLE ranking_entradas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    tipo_entidad VARCHAR(20) NOT NULL,
    id_entidad BIGINT NOT NULL,
    nombre_entidad VARCHAR(100) NOT NULL,

    region VARCHAR(20) NOT NULL,

    puntos DOUBLE NOT NULL,
    posicion INT,

    victorias INT,
    derrotas INT,

    fecha_actualizacion DATETIME NOT NULL,

    CONSTRAINT uk_ranking_entidad_region
        UNIQUE (tipo_entidad, id_entidad, region)
);