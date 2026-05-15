CREATE TABLE partidas (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    id_torneo BIGINT NOT NULL,
    nombre_torneo VARCHAR(150),

    id_equipo_local BIGINT NOT NULL,
    nombre_equipo_local VARCHAR(80),

    id_equipo_visitante BIGINT NOT NULL,
    nombre_equipo_visitante VARCHAR(80),

    fecha_hora DATETIME NOT NULL,

    duracion_minutos INT,

    marcador_local INT,
    marcador_visitante INT,

    id_equipo_ganador BIGINT,

    estado VARCHAR(30) NOT NULL,

    mapa_o_escenario VARCHAR(80)
);