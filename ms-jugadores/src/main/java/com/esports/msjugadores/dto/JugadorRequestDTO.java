package com.esports.msjugadores.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JugadorRequestDTO {

    @NotBlank(message = "El nickname es obligatorio")
    @Size(min = 2, max = 50, message = "El nickname debe tener entre 2 y 50 caracteres")
    private String nickname;

    @NotBlank(message = "El nombre real es obligatorio")
    @Size(max = 100, message = "El nombre real no puede superar los 100 caracteres")
    private String nombreReal;

    @NotBlank(message = "El país es obligatorio")
    @Size(max = 50, message = "El país no puede superar los 50 caracteres")
    private String pais;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe ser en el pasado")
    private LocalDate fechaNacimiento;

    @NotBlank(message = "El rol es obligatorio")
    @Size(max = 30, message = "El rol no puede superar los 30 caracteres")
    private String rol;

    // Opcional: puede ser null si es free agent (sin equipo)
    private Long idEquipoActual;

    @PositiveOrZero(message = "El salario no puede ser negativo")
    private Double salarioMensualUsd;
}
