package com.esports.msequipos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "equipos", uniqueConstraints = @UniqueConstraint(columnNames = { "nombre", "region" }))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 80)
    private String nombre;

    // Región: LATAM, NA, EMEA, KR, CN, etc.
    @Column(name = "region", nullable = false, length = 20)
    private String region;

    @Column(name = "fecha_fundacion", nullable = false)
    private LocalDate fechaFundacion;

    @Column(name = "ranking_mundial")
    private Integer rankingMundial;

    @Column(name = "presupuesto_anual_usd")
    private Double presupuestoAnualUsd;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<StaffTecnico> staff = new ArrayList<>();

    @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<RosterHistorico> roster = new ArrayList<>();

    public void agregarStaff(StaffTecnico s) {
        staff.add(s);
        s.setEquipo(this);
    }

    public void agregarRoster(RosterHistorico r) {
        roster.add(r);
        r.setEquipo(this);
    }
}
