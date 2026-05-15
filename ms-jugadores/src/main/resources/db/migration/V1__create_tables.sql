CREATE TABLE jugadores (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nickname VARCHAR(50) NOT NULL UNIQUE,
    nombre_real VARCHAR(100) NOT NULL,
    pais VARCHAR(50) NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    rol VARCHAR(30) NOT NULL,
    id_equipo_actual BIGINT,
    activo BOOLEAN NOT NULL,
    salario_mensual_usd DOUBLE
);