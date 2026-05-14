CREATE TABLE ranking_entradas (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    tipo_entidad         VARCHAR(20)  NOT NULL,
    id_entidad           BIGINT       NOT NULL,
    nombre_entidad       VARCHAR(100) NOT NULL,
    region               VARCHAR(20)  NOT NULL,
    puntos               DOUBLE       NOT NULL,
    posicion             INT          NULL,
    victorias            INT          NULL,
    derrotas             INT          NULL,
    fecha_actualizacion  DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_ranking_tipo_entidad_region UNIQUE (tipo_entidad, id_entidad, region)
) ENGINE=InnoDB;
