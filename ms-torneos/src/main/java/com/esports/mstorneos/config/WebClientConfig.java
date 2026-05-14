package com.esports.mstorneos.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${microservicios.ms-juegos.url}")
    private String urlMsJuegos;

    @Bean
    public WebClient webClientJuegos() {
        return WebClient.builder().baseUrl(urlMsJuegos).build();
    }
}
