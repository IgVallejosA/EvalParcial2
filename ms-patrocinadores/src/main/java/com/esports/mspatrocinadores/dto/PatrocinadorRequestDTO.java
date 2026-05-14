package com.esports.mspatrocinadores.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class PatrocinadorRequestDTO {

    @NotBlank
    @Size(min = 2, max = 100)
    private String nombreEmpresa;

    @NotBlank
    @Size(max = 50)
    private String industria;

    @NotBlank
    @Size(max = 50)
    private String paisOrigen;

    @Size(max = 200)
    private String sitioWeb;

    @NotBlank
    @Pattern(regexp = "PLATINUM|GOLD|SILVER|BRONZE",
             message = "Tier: PLATINUM, GOLD, SILVER o BRONZE")
    private String tier;
}
