package com.esports.msestadisticas.controller;

import com.esports.msestadisticas.dto.*;
import com.esports.msestadisticas.service.EstadisticaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/estadisticas")
@RequiredArgsConstructor
public class EstadisticaController {

    private static final Logger log = LoggerFactory.getLogger(EstadisticaController.class);

    private final EstadisticaService estadisticaService;

    @PostMapping
    public ResponseEntity<EstadisticaResponseDTO> registrar(@Valid @RequestBody EstadisticaRequestDTO dto) {
        log.info("POST /estadisticas - Registrando estadística para jugador/partida");
        return ResponseEntity.status(HttpStatus.CREATED).body(estadisticaService.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<EstadisticaResponseDTO>> listar() {
        log.debug("GET /estadisticas - Listando todas las estadísticas");
        return ResponseEntity.ok(estadisticaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadisticaResponseDTO> buscarPorId(@PathVariable Long id) {
        log.info("GET /estadisticas/{} - Buscando estadística por ID", id);
        return ResponseEntity.ok(estadisticaService.buscarPorId(id));
    }

    @GetMapping("/jugador/{idJugador}")
    public ResponseEntity<List<EstadisticaResponseDTO>> buscarPorJugador(@PathVariable Long idJugador) {
        log.info("GET /estadisticas/jugador/{} - Buscando estadísticas del jugador", idJugador);
        return ResponseEntity.ok(estadisticaService.buscarPorJugador(idJugador));
    }

    @GetMapping("/partida/{idPartida}")
    public ResponseEntity<List<EstadisticaResponseDTO>> buscarPorPartida(@PathVariable Long idPartida) {
        log.info("GET /estadisticas/partida/{} - Buscando estadísticas de la partida", idPartida);
        return ResponseEntity.ok(estadisticaService.buscarPorPartida(idPartida));
    }

    @GetMapping("/jugador/{idJugador}/resumen")
    public ResponseEntity<ResumenJugadorDTO> resumenJugador(@PathVariable Long idJugador) {
        log.info("GET /estadisticas/jugador/{}/resumen - Generando resumen del jugador", idJugador);
        return ResponseEntity.ok(estadisticaService.obtenerResumenJugador(idJugador));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /estadisticas/{} - Eliminando estadística (operación destructiva)", id);
        estadisticaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
