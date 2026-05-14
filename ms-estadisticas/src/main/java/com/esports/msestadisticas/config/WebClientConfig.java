package com.esports.msestadisticas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservicios.ms-jugadores.url}")
    private String urlMsJugadores;

    @Value("${microservicios.ms-partidas.url}")
    private String urlMsPartidas;

    @Bean
    public WebClient webClientJugadores() {
        return WebClient.builder().baseUrl(urlMsJugadores).build();
    }

    @Bean
    public WebClient webClientPartidas() {
        return WebClient.builder().baseUrl(urlMsPartidas).build();
    }
}
