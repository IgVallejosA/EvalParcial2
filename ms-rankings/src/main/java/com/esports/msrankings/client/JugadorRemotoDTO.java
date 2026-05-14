package com.esports.msrankings.client;

import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class JugadorRemotoDTO {
    private Long id;
    private String nickname;
    private Boolean activo;
}
