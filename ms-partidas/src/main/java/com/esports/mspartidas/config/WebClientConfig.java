package com.esports.mspartidas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservicios.ms-torneos.url}")
    private String urlMsTorneos;

    @Value("${microservicios.ms-equipos.url}")
    private String urlMsEquipos;

    @Bean
    public WebClient webClientTorneos() {
        return WebClient.builder().baseUrl(urlMsTorneos).build();
    }

    @Bean
    public WebClient webClientEquipos() {
        return WebClient.builder().baseUrl(urlMsEquipos).build();
    }
}
