package com.esports.msrankings.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ranking_entradas", uniqueConstraints = @UniqueConstraint(columnNames = { "tipo_entidad", "id_entidad",
        "region" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RankingEntrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tipo_entidad", nullable = false, length = 20)
    private String tipoEntidad; // EQUIPO, JUGADOR

    @Column(name = "id_entidad", nullable = false)
    private Long idEntidad;

    @Column(name = "nombre_entidad", nullable = false, length = 100)
    private String nombreEntidad;

    @Column(name = "region", nullable = false, length = 20)
    private String region;

    @Column(name = "puntos", nullable = false)
    private Double puntos;

    @Column(name = "posicion")
    private Integer posicion;

    @Column(name = "victorias")
    private Integer victorias;

    @Column(name = "derrotas")
    private Integer derrotas;

    @Column(name = "fecha_actualizacion", nullable = false)
    private LocalDateTime fechaActualizacion;
}
