package com.esports.msjugadores.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "jugadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jugador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false, unique = true, length = 50)
    private String nickname;

    @Column(name = "nombre_real", nullable = false, length = 100)
    private String nombreReal;

    @Column(name = "pais", nullable = false, length = 50)
    private String pais;

    @Column(name = "fecha_nacimiento", nullable = false)
    private LocalDate fechaNacimiento;

    @Column(name = "rol", nullable = false, length = 30)
    private String rol;

    @Column(name = "id_equipo_actual")
    private Long idEquipoActual;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "salario_mensual_usd")
    private Double salarioMensualUsd;
}
