package com.esports.msjuegos.dto;

import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JuegoResponseDTO {
    private Long id;
    private String nombre;
    private String genero;
    private String desarrolladora;
    private LocalDate fechaLanzamiento;
    private Integer aniosEnMercado;
    private String plataforma;
    private Double prizePoolTotalUsd;
    private Boolean activo;
    private Integer cantidadModos;
    private List<ModoCompetitivoResponseDTO> modos;
}
