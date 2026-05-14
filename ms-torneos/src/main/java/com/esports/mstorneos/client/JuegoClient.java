package com.esports.mstorneos.client;

import com.esports.mstorneos.exception.ComunicacionMicroservicioException;
import com.esports.mstorneos.exception.RecursoNoEncontradoException;
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
public class JuegoClient {

    private static final Logger log = LoggerFactory.getLogger(JuegoClient.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final WebClient webClientJuegos;

    public JuegoRemotoDTO obtenerJuegoPorId(Long idJuego) {
        log.info("Consultando ms-juegos para juego ID: {}", idJuego);
        try {
            JuegoRemotoDTO juego = webClientJuegos.get()
                    .uri("/{id}", idJuego)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, response ->
                        Mono.error(new RecursoNoEncontradoException(
                                "Juego con ID " + idJuego + " no existe en ms-juegos")))
                    .onStatus(HttpStatusCode::is5xxServerError, response ->
                        Mono.error(new ComunicacionMicroservicioException(
                                "ms-juegos no está disponible")))
                    .bodyToMono(JuegoRemotoDTO.class)
                    .timeout(TIMEOUT)
                    .block();

            log.info("Juego remoto obtenido: {}", juego.getNombre());
            return juego;

        } catch (RecursoNoEncontradoException | ComunicacionMicroservicioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Falla al consultar ms-juegos: {}", e.getMessage(), e);
            throw new ComunicacionMicroservicioException(
                    "No se pudo comunicar con ms-juegos: " + e.getMessage());
        }
    }
}
