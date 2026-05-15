package com.esports.mstorneos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "fases_torneo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FaseTorneo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_fase", nullable = false, length = 50)
    private String nombreFase;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "formato", length = 30)
    private String formato; // BO1, BO3, BO5, ROUND_ROBIN

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "torneo_id", nullable = false)
    @JsonBackReference
    private Torneo torneo;
}
