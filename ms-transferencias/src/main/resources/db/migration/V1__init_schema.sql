CREATE TABLE transferencias (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,

    id_jugador BIGINT NOT NULL,
    nickname_jugador VARCHAR(50),

    id_equipo_origen BIGINT,
    nombre_equipo_origen VARCHAR(80),

    id_equipo_destino BIGINT,
    nombre_equipo_destino VARCHAR(80),

    fecha_transferencia DATE NOT NULL,

    monto_usd DOUBLE,

    tipo VARCHAR(30) NOT NULL,

    duracion_contrato_meses INT,

    observaciones VARCHAR(500)
);