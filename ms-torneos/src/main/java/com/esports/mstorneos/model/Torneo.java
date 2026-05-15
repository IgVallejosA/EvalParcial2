package com.esports.mstorneos.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "torneos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Torneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "id_juego", nullable = false)
    private Long idJuego;

    @Column(name = "nombre_juego", length = 100)
    private String nombreJuego;

    @Column(name = "organizador", nullable = false, length = 100)
    private String organizador;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "premio_total_usd", nullable = false)
    private Double premioTotalUsd;

    @Column(name = "max_equipos", nullable = false)
    private Integer maxEquipos;

    @Column(name = "modalidad", nullable = false, length = 30)
    private String modalidad; // PRESENCIAL, ONLINE, HIBRIDO

    @Column(name = "estado", nullable = false, length = 30)
    private String estado; // PLANIFICADO, EN_CURSO, FINALIZADO, CANCELADO

    @OneToMany(mappedBy = "torneo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<FaseTorneo> fases = new ArrayList<>();

    public void agregarFase(FaseTorneo f) {
        fases.add(f);
        f.setTorneo(this);
    }
}
