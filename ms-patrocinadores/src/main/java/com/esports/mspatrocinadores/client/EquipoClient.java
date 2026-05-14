package com.esports.mspatrocinadores.client;

import com.esports.mspatrocinadores.exception.ComunicacionMicroservicioException;
import com.esports.mspatrocinadores.exception.RecursoNoEncontradoException;
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
public class EquipoClient {

    private static final Logger log = LoggerFactory.getLogger(EquipoClient.class);
    private static final Duration TIMEOUT = Duration.ofSeconds(5);

    private final WebClient webClientEquipos;

    public EquipoRemotoDTO obtenerEquipoPorId(Long id) {
        log.info("Consultando ms-equipos para equipo ID: {}", id);
        try {
            return webClientEquipos.get()
                    .uri("/{id}", id)
                    .retrieve()
                    .onStatus(HttpStatusCode::is4xxClientError, r ->
                        Mono.error(new RecursoNoEncontradoException(
                                "Equipo " + id + " no existe en ms-equipos")))
                    .onStatus(HttpStatusCode::is5xxServerError, r ->
                        Mono.error(new ComunicacionMicroservicioException(
                                "ms-equipos no disponible")))
                    .bodyToMono(EquipoRemotoDTO.class)
                    .timeout(TIMEOUT)
                    .block();
        } catch (RecursoNoEncontradoException | ComunicacionMicroservicioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Falla ms-equipos: {}", e.getMessage());
            throw new ComunicacionMicroservicioException("No se pudo comunicar con ms-equipos");
        }
    }
}
