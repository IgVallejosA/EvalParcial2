package com.esports.mstorneos.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class FaseRequestDTO {

    @NotBlank
    @Size(max = 50)
    private String nombreFase;

    @NotNull
    @Positive
    private Integer orden;

    @NotNull
    private LocalDate fechaInicio;

    @NotNull
    private LocalDate fechaFin;

    @NotBlank
    @Pattern(regexp = "BO1|BO3|BO5|ROUND_ROBIN",
             message = "Formato: BO1, BO3, BO5 o ROUND_ROBIN")
    private String formato;
}
