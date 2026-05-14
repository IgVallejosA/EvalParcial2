package com.esports.msstreamers.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "asignaciones_cobertura")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class AsignacionCobertura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_torneo", nullable = false)
    private Long idTorneo;

    @Column(name = "nombre_torneo", length = 150)
    private String nombreTorneo;

    @Column(name = "fecha_inicio_cobertura", nullable = false)
    private LocalDate fechaInicioCobertura;

    @Column(name = "fecha_fin_cobertura", nullable = false)
    private LocalDate fechaFinCobertura;

    @Column(name = "honorarios_usd")
    private Double honorariosUsd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "streamer_id", nullable = false)
    @JsonBackReference
    private Streamer streamer;
}
