package com.esports.msstreamers.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class StreamerRequestDTO {

    @NotBlank
    @Size(min = 2, max = 80)
    private String nombreArtistico;

    @Size(max = 100)
    private String nombreReal;

    @NotBlank
    @Size(max = 50)
    private String pais;

    @NotBlank
    @Pattern(regexp = "ES|EN|PT|KR|ZH|JP|FR|DE",
             message = "Idioma: ES, EN, PT, KR, ZH, JP, FR o DE")
    private String idioma;

    @NotBlank
    @Pattern(regexp = "CASTER|ANALYST|INTERVIEWER|HOST",
             message = "Rol: CASTER, ANALYST, INTERVIEWER o HOST")
    private String rol;

    @NotBlank
    @Pattern(regexp = "TWITCH|YOUTUBE|KICK|AFREECA",
             message = "Plataforma: TWITCH, YOUTUBE, KICK o AFREECA")
    private String plataformaPrincipal;

    @PositiveOrZero
    private Integer seguidores;
}
