package com.esports.msequipos.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class StaffRequestDTO {

    @NotBlank(message = "El nombre del staff es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El rol del staff es obligatorio")
    @Pattern(regexp = "HEAD_COACH|ASSISTANT_COACH|ANALYST|MANAGER|PSYCHOLOGIST",
             message = "Rol inválido. Valores permitidos: HEAD_COACH, ASSISTANT_COACH, ANALYST, MANAGER, PSYCHOLOGIST")
    private String rolStaff;

    @PositiveOrZero(message = "El salario no puede ser negativo")
    private Double salarioMensualUsd;
}
