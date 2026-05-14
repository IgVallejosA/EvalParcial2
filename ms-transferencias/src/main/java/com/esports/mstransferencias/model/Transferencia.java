package com.esports.mstransferencias.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "transferencias")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Transferencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_jugador", nullable = false)
    private Long idJugador;

    @Column(name = "nickname_jugador", length = 50)
    private String nicknameJugador;

    @Column(name = "id_equipo_origen")
    private Long idEquipoOrigen;

    @Column(name = "nombre_equipo_origen", length = 80)
    private String nombreEquipoOrigen;

    @Column(name = "id_equipo_destino")
    private Long idEquipoDestino;

    @Column(name = "nombre_equipo_destino", length = 80)
    private String nombreEquipoDestino;

    @Column(name = "fecha_transferencia", nullable = false)
    private LocalDate fechaTransferencia;

    @Column(name = "monto_usd")
    private Double montoUsd;

    @Column(name = "tipo", nullable = false, length = 30)
    private String tipo; 

    @Column(name = "duracion_contrato_meses")
    private Integer duracionContratoMeses;

    @Column(name = "observaciones", length = 500)
    private String observaciones;
}
