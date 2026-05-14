package com.esports.mstransferencias.controller;

import com.esports.mstransferencias.dto.*;
import com.esports.mstransferencias.service.TransferenciaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transferencias")
@RequiredArgsConstructor
public class TransferenciaController {

    private static final Logger log = LoggerFactory.getLogger(TransferenciaController.class);

    private final TransferenciaService transferenciaService;

    @PostMapping
    public ResponseEntity<TransferenciaResponseDTO> registrar(@Valid @RequestBody TransferenciaRequestDTO dto) {
        log.info("POST /transferencias - Registrando transferencia del jugador {} al equipo {}",
                dto.getIdJugador(), dto.getIdEquipoDestino());
        return ResponseEntity.status(HttpStatus.CREATED).body(transferenciaService.registrar(dto));
    }

    @GetMapping
    public ResponseEntity<List<TransferenciaResponseDTO>> listar() {
        log.debug("GET /transferencias - Listando todas las transferencias");
        return ResponseEntity.ok(transferenciaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferenciaResponseDTO> buscarPorId(@PathVariable Long id) {
        log.info("GET /transferencias/{} - Buscando transferencia por ID", id);
        return ResponseEntity.ok(transferenciaService.buscarPorId(id));
    }

    @GetMapping("/jugador/{idJugador}/historial")
    public ResponseEntity<List<TransferenciaResponseDTO>> historialJugador(@PathVariable Long idJugador) {
        log.info("GET /transferencias/jugador/{}/historial - Historial del jugador", idJugador);
        return ResponseEntity.ok(transferenciaService.historialJugador(idJugador));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<TransferenciaResponseDTO>> buscarPorTipo(@RequestParam String tipo) {
        log.info("GET /transferencias/buscar?tipo={} - Filtrando por tipo", tipo);
        return ResponseEntity.ok(transferenciaService.buscarPorTipo(tipo));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /transferencias/{} - Eliminando transferencia (destructivo)", id);
        transferenciaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
