CREATE TABLE transferencias (
    id                       BIGINT       NOT NULL AUTO_INCREMENT,
    id_jugador               BIGINT       NOT NULL,
    nickname_jugador         VARCHAR(50)  NULL,
    id_equipo_origen         BIGINT       NULL,
    nombre_equipo_origen     VARCHAR(80)  NULL,
    id_equipo_destino        BIGINT       NULL,
    nombre_equipo_destino    VARCHAR(80)  NULL,
    fecha_transferencia      DATE         NOT NULL,
    monto_usd                DOUBLE       NULL,
    tipo                     VARCHAR(30)  NOT NULL,
    duracion_contrato_meses  INT          NULL,
    observaciones            VARCHAR(500) NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;
