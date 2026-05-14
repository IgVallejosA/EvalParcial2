package com.esports.msrankings.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservicios.ms-equipos.url}")
    private String urlEquipos;

    @Value("${microservicios.ms-jugadores.url}")
    private String urlJugadores;

    @Bean
    public WebClient webClientEquipos() {
        return WebClient.builder().baseUrl(urlEquipos).build();
    }

    @Bean
    public WebClient webClientJugadores() {
        return WebClient.builder().baseUrl(urlJugadores).build();
    }
}
