package com.esports.msequipos.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class EquipoRequestDTO {

    @NotBlank(message = "El nombre del equipo es obligatorio")
    @Size(min = 2, max = 80, message = "El nombre debe tener entre 2 y 80 caracteres")
    private String nombre;

    @NotBlank(message = "La región es obligatoria")
    @Size(max = 20)
    private String region;

    @NotNull(message = "La fecha de fundación es obligatoria")
    @PastOrPresent(message = "La fecha de fundación no puede ser futura")
    private LocalDate fechaFundacion;

    @Positive(message = "El ranking mundial debe ser positivo")
    private Integer rankingMundial;

    @PositiveOrZero(message = "El presupuesto no puede ser negativo")
    private Double presupuestoAnualUsd;
}
