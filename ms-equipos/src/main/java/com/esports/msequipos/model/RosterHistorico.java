package com.esports.msequipos.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "roster_historico")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RosterHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_jugador", nullable = false)
    private Long idJugador;

    // Mantener el nickname que usaba en ese equipo.
    @Column(name = "nickname_al_unirse", length = 50)
    private String nicknameAlUnirse;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    // null = sigue activo en el equipo
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipo_id", nullable = false)
    @JsonBackReference
    private Equipo equipo;
}
