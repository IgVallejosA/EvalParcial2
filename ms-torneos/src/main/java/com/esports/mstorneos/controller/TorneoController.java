package com.esports.mstorneos.controller;

import com.esports.mstorneos.dto.*;
import com.esports.mstorneos.service.TorneoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/torneos")
@RequiredArgsConstructor
public class TorneoController {

    private static final Logger log = LoggerFactory.getLogger(TorneoController.class);

    private final TorneoService torneoService;

    @PostMapping
    public ResponseEntity<TorneoResponseDTO> crear(@Valid @RequestBody TorneoRequestDTO dto) {
        log.info("POST /torneos - Creando torneo: {}", dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(torneoService.crearTorneo(dto));
    }

    @GetMapping
    public ResponseEntity<List<TorneoResponseDTO>> listar() {
        log.debug("GET /torneos - Listando todos los torneos");
        return ResponseEntity.ok(torneoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TorneoResponseDTO> buscarPorId(@PathVariable Long id) {
        log.info("GET /torneos/{} - Buscando torneo por ID", id);
        return ResponseEntity.ok(torneoService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<TorneoResponseDTO>> buscarPorEstado(@RequestParam String estado) {
        log.info("GET /torneos/buscar?estado={} - Buscando torneos por estado", estado);
        return ResponseEntity.ok(torneoService.buscarPorEstado(estado));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<TorneoResponseDTO> cambiarEstado(
            @PathVariable Long id,
            @RequestParam String nuevoEstado) {
        log.warn("PATCH /torneos/{}/estado={} - Cambiando estado del torneo", id, nuevoEstado);
        return ResponseEntity.ok(torneoService.cambiarEstado(id, nuevoEstado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /torneos/{} - Eliminando torneo (destructivo)", id);
        torneoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idTorneo}/fases")
    public ResponseEntity<FaseResponseDTO> agregarFase(
            @PathVariable Long idTorneo,
            @Valid @RequestBody FaseRequestDTO dto) {
        log.info("POST /torneos/{}/fases - Agregando fase: {}", idTorneo);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(torneoService.agregarFase(idTorneo, dto));
    }
}
