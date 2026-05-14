package com.esports.msstreamers.client;

import com.esports.msstreamers.exception.ComunicacionMicroservicioException;
import com.esports.msstreamers.exception.RecursoNoEncontradoException;
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
public class TorneoClient {

    private static final Logger log = LoggerFactory.getLogger(TorneoClient.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final WebClient webClientTorneos;

    public TorneoRemotoDTO obtenerTorneoPorId(Long id) {
        try {
            return webClientTorneos.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, r ->
                        Mono.error(new RecursoNoEncontradoException(
                                "Torneo " + id + " no existe en ms-torneos")))
                    .onStatus(HttpStatusCode::is5xxServerError, r ->
                        Mono.error(new ComunicacionMicroservicioException(
                                "ms-torneos no disponible")))
                    .bodyToMono(TorneoRemotoDTO.class)
                    .timeout(TIMEOUT)
                    .block();
        } catch (RecursoNoEncontradoException | ComunicacionMicroservicioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Falla ms-torneos: {}", e.getMessage());
            throw new ComunicacionMicroservicioException("No se pudo comunicar con ms-torneos");
        }
    }
}
