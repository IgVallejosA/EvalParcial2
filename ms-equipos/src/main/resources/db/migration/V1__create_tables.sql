CREATE TABLE equipos (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(80) NOT NULL,
    region VARCHAR(20) NOT NULL,
    fecha_fundacion DATE NOT NULL,
    ranking_mundial INT,
    presupuesto_anual_usd DOUBLE,
    activo BOOLEAN NOT NULL,

    CONSTRAINT uk_equipo_nombre_region
        UNIQUE (nombre, region)
);

CREATE TABLE staff_tecnico (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    rol_staff VARCHAR(30) NOT NULL,
    salario_mensual_usd DOUBLE,
    activo BOOLEAN NOT NULL,

    equipo_id BIGINT NOT NULL,

    CONSTRAINT fk_staff_equipo
        FOREIGN KEY (equipo_id)
        REFERENCES equipos(id)
        ON DELETE CASCADE
);

CREATE TABLE roster_historico (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    id_jugador BIGINT NOT NULL,
    nickname_al_unirse VARCHAR(50),
    fecha_inicio DATE NOT NULL,
    fecha_fin DATE,

    equipo_id BIGINT NOT NULL,

    CONSTRAINT fk_roster_equipo
        FOREIGN KEY (equipo_id)
        REFERENCES equipos(id)
        ON DELETE CASCADE
);