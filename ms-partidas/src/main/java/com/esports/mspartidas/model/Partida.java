package com.esports.mspartidas.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "partidas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Partida {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_torneo", nullable = false)
    private Long idTorneo;

    @Column(name = "nombre_torneo", length = 150)
    private String nombreTorneo;

    @Column(name = "id_equipo_local", nullable = false)
    private Long idEquipoLocal;

    @Column(name = "nombre_equipo_local", length = 80)
    private String nombreEquipoLocal;

    @Column(name = "id_equipo_visitante", nullable = false)
    private Long idEquipoVisitante;

    @Column(name = "nombre_equipo_visitante", length = 80)
    private String nombreEquipoVisitante;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Column(name = "marcador_local")
    private Integer marcadorLocal;

    @Column(name = "marcador_visitante")
    private Integer marcadorVisitante;

    @Column(name = "id_equipo_ganador")
    private Long idEquipoGanador;

    @Column(name = "estado", nullable = false, length = 30)
    private String estado; // EN_CURSO, FINALIZADA, CANCELADA

    @Column(name = "mapa_o_escenario", length = 80)
    private String mapaOEscenario;
}
