package com.esports.msestadisticas.client;

import com.esports.msestadisticas.exception.ComunicacionMicroservicioException;
import com.esports.msestadisticas.exception.RecursoNoEncontradoException;
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
public class PartidaClient {

    private static final Logger log = LoggerFactory.getLogger(PartidaClient.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final WebClient webClientPartidas;

    public PartidaRemotoDTO obtenerPartidaPorId(Long id) {
        try {
            return webClientPartidas.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, r ->
                        Mono.error(new RecursoNoEncontradoException(
                                "Partida " + id + " no existe en ms-partidas")))
                    .onStatus(HttpStatusCode::is5xxServerError, r ->
                        Mono.error(new ComunicacionMicroservicioException(
                                "ms-partidas no disponible")))
                    .bodyToMono(PartidaRemotoDTO.class)
                    .timeout(TIMEOUT)
                    .block();
        } catch (RecursoNoEncontradoException | ComunicacionMicroservicioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Falla ms-partidas: {}", e.getMessage());
            throw new ComunicacionMicroservicioException("No se pudo comunicar con ms-partidas");
        }
    }
}
