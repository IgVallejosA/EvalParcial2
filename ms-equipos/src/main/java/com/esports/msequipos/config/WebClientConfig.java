package com.esports.msequipos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservicios.ms-jugadores.url}")
    private String urlMsJugadores;

    @Bean
    public WebClient webClientJugadores() {
        return WebClient.builder()
                .baseUrl(urlMsJugadores)
                .build();
    }
}
