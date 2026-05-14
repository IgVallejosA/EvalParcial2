package com.esports.msrankings.client;

import com.esports.msrankings.exception.ComunicacionMicroservicioException;
import com.esports.msrankings.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class JugadorClient {

    private static final Logger log = LoggerFactory.getLogger(JugadorClient.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final WebClient webClientJugadores;

    public JugadorRemotoDTO obtenerJugadorPorId(Long id) {
        try {
            return webClientJugadores.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, r ->
                        Mono.error(new RecursoNoEncontradoException(
                                "Jugador " + id + " no existe en ms-jugadores")))
                    .onStatus(HttpStatusCode::is5xxServerError, r ->
                        Mono.error(new ComunicacionMicroservicioException(
                                "ms-jugadores no disponible")))
                    .bodyToMono(JugadorRemotoDTO.class)
                    .timeout(TIMEOUT)
                    .block();
        } catch (RecursoNoEncontradoException | ComunicacionMicroservicioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Falla ms-jugadores: {}", e.getMessage());
            throw new ComunicacionMicroservicioException("No se pudo comunicar con ms-jugadores");
        }
    }
}
