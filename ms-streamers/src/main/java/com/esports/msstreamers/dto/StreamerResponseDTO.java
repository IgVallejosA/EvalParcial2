package com.esports.msstreamers.dto;

import lombok.*;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class StreamerResponseDTO {
    private Long id;
    private String nombreArtistico;
    private String nombreReal;
    private String pais;
    private String idioma;
    private String rol;
    private String plataformaPrincipal;
    private Integer seguidores;
    private Boolean activo;
    private Integer cantidadCoberturas;
    private List<AsignacionResponseDTO> coberturas;
}
