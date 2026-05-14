CREATE TABLE jugadores (
    id                   BIGINT       NOT NULL AUTO_INCREMENT,
    nickname             VARCHAR(50)  NOT NULL,
    nombre_real          VARCHAR(100) NOT NULL,
    pais                 VARCHAR(50)  NOT NULL,
    fecha_nacimiento     DATE         NOT NULL,
    rol                  VARCHAR(30)  NOT NULL,
    id_equipo_actual     BIGINT       NULL,
    activo               BIT          NOT NULL,
    salario_mensual_usd  DOUBLE       NULL,
    PRIMARY KEY (id),
    CONSTRAINT uk_jugadores_nickname UNIQUE (nickname)
) ENGINE=InnoDB;
