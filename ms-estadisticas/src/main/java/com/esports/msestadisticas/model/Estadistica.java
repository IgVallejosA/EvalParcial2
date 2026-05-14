package com.esports.msestadisticas.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "estadisticas", uniqueConstraints = @UniqueConstraint(columnNames = { "id_jugador", "id_partida" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Estadistica {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_jugador", nullable = false)
    private Long idJugador;

    @Column(name = "nickname_jugador", length = 50)
    private String nicknameJugador;

    @Column(name = "id_partida", nullable = false)
    private Long idPartida;

    @Column(name = "kills", nullable = false)
    private Integer kills;

    @Column(name = "deaths", nullable = false)
    private Integer deaths;

    @Column(name = "assists", nullable = false)
    private Integer assists;

    @Column(name = "kda")
    private Double kda;

    @Column(name = "damage_dealt")
    private Integer damageDealt;

    @Column(name = "tiempo_jugado_minutos")
    private Integer tiempoJugadoMinutos;

    @Column(name = "mvp", nullable = false)
    private Boolean mvp;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;
}
