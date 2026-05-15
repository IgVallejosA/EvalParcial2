package com.esports.msequipos.client;

import com.esports.msequipos.exception.ComunicacionMicroservicioException;
import com.esports.msequipos.exception.RecursoNoEncontradoException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Component
@RequiredArgsConstructor
public class JugadorClient {

    private static final Logger log = LoggerFactory.getLogger(JugadorClient.class);

    private final WebClient webClientJugadores;

    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    public JugadorRemotoDTO obtenerJugadorPorId(Long idJugador) {
        log.info("Consultando ms-jugadores para obtener jugador ID: {}", idJugador);

        try {
            JugadorRemotoDTO jugador = webClientJugadores.get()
                    .uri("/{id}", idJugador)
                    .retrieve()

                    .onStatus(HttpStatusCode::is4xxClientError, response -> {
                        log.warn("ms-jugadores respondió 4xx para ID {}: {}", idJugador, response.statusCode());
                        return Mono.error(new RecursoNoEncontradoException(
                                "Jugador con ID " + idJugador + " no existe en ms-jugadores"));
                    })
                    .onStatus(HttpStatusCode::is5xxServerError, response -> {
                        log.error("ms-jugadores respondió 5xx para ID {}: {}", idJugador, response.statusCode());
                        return Mono.error(new ComunicacionMicroservicioException(
                                "ms-jugadores no está disponible en este momento"));
                    })
                    .bodyToMono(JugadorRemotoDTO.class)
                    .timeout(TIMEOUT)
                    .block();

            log.info("Jugador remoto obtenido: ID={}, nickname={}", jugador.getId(), jugador.getNickname());
            return jugador;

        } catch (RecursoNoEncontradoException | ComunicacionMicroservicioException e) {
            throw e;
        } catch (WebClientResponseException e) {
            log.error("Error HTTP al consultar ms-jugadores: status={}, body={}", e.getStatusCode(),
                    e.getResponseBodyAsString());
            throw new ComunicacionMicroservicioException(
                    "Error al comunicarse con ms-jugadores: " + e.getStatusCode());
        } catch (Exception e) {
            log.error("Falla de red o timeout al consultar ms-jugadores: {}", e.getMessage(), e);
            throw new ComunicacionMicroservicioException(
                    "No se pudo establecer comunicación con ms-jugadores: " + e.getMessage());
        }
    }
}
