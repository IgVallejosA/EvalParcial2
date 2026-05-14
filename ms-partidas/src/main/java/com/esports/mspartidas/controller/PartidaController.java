package com.esports.mspartidas.controller;

import com.esports.mspartidas.dto.*;
import com.esports.mspartidas.service.PartidaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/partidas")
@RequiredArgsConstructor
public class PartidaController {

    private static final Logger log = LoggerFactory.getLogger(PartidaController.class);

    private final PartidaService partidaService;

    @PostMapping
    public ResponseEntity<PartidaResponseDTO> programar(@Valid @RequestBody PartidaRequestDTO dto) {
        log.info("POST /partidas - Programando partida entre equipoLocalId={} y equipoVisitanteId={}",
                dto.getIdEquipoLocal(), dto.getIdEquipoVisitante());
        return ResponseEntity.status(HttpStatus.CREATED).body(partidaService.programarPartida(dto));
    }

    @GetMapping
    public ResponseEntity<List<PartidaResponseDTO>> listar() {
        log.debug("GET /partidas - Listando todas las partidas");
        return ResponseEntity.ok(partidaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PartidaResponseDTO> buscarPorId(@PathVariable Long id) {
        log.info("GET /partidas/{} - Buscando partida por ID", id);
        return ResponseEntity.ok(partidaService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PartidaResponseDTO>> buscarPorTorneo(@RequestParam Long idTorneo) {
        log.info("GET /partidas/buscar?idTorneo={} - Listando partidas del torneo", idTorneo);
        return ResponseEntity.ok(partidaService.buscarPorTorneo(idTorneo));
    }

    @PatchMapping("/{id}/resultado")
    public ResponseEntity<PartidaResponseDTO> registrarResultado(
            @PathVariable Long id,
            @Valid @RequestBody ResultadoRequestDTO dto) {
        log.info("PATCH /partidas/{}/resultado - Registrando resultado: {} - {}");
        return ResponseEntity.ok(partidaService.registrarResultado(id, dto));
    }

    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<PartidaResponseDTO> cancelar(@PathVariable Long id) {
        log.warn("PATCH /partidas/{}/cancelar - Cancelando partida", id);
        return ResponseEntity.ok(partidaService.cancelar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /partidas/{} - Eliminando partida (operación destructiva)", id);
        partidaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
