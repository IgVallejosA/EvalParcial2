package com.esports.mspatrocinadores.dto;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PatrocinadorResponseDTO {
    private Long id;
    private String nombreEmpresa;
    private String industria;
    private String paisOrigen;
    private String sitioWeb;
    private String tier;
    private Boolean activo;
    private Integer cantidadContratos;
    private List<ContratoResponseDTO> contratos;
}
