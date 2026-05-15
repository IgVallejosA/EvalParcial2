package com.esports.msjuegos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "modos_competitivos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ModoCompetitivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_modo", nullable = false, length = 80)
    private String nombreModo;

    @Column(name = "max_jugadores_por_equipo", nullable = false)
    private Integer maxJugadoresPorEquipo;

    @Column(name = "duracion_promedio_minutos")
    private Integer duracionPromedioMinutos;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "juego_id", nullable = false)
    @JsonBackReference
    private Juego juego;
}
