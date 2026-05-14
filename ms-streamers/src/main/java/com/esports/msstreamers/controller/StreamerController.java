package com.esports.msstreamers.controller;

import com.esports.msstreamers.dto.*;
import com.esports.msstreamers.service.StreamerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/streamers")
@RequiredArgsConstructor
public class StreamerController {

    private static final Logger log = LoggerFactory.getLogger(StreamerController.class);

    private final StreamerService streamerService;

    @PostMapping
    public ResponseEntity<StreamerResponseDTO> crear(@Valid @RequestBody StreamerRequestDTO dto) {
        log.info("POST /streamers - Creando streamer: {}");
        return ResponseEntity.status(HttpStatus.CREATED).body(streamerService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<StreamerResponseDTO>> listar() {
        log.debug("GET /streamers - Listando todos los streamers");
        return ResponseEntity.ok(streamerService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StreamerResponseDTO> buscarPorId(@PathVariable Long id) {
        log.info("GET /streamers/{} - Buscando streamer por ID", id);
        return ResponseEntity.ok(streamerService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<StreamerResponseDTO>> buscarPorIdioma(@RequestParam String idioma) {
        log.info("GET /streamers/buscar?idioma={} - Buscando streamers por idioma", idioma);
        return ResponseEntity.ok(streamerService.buscarPorIdioma(idioma));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StreamerResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody StreamerRequestDTO dto) {
        log.info("PUT /streamers/{} - Actualizando streamer: {}", id);
        return ResponseEntity.ok(streamerService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        log.warn("PATCH /streamers/{}/desactivar - Desactivando streamer", id);
        streamerService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /streamers/{} - Eliminando streamer (destructivo)", id);
        streamerService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idStreamer}/coberturas")
    public ResponseEntity<AsignacionResponseDTO> asignarCobertura(
            @PathVariable Long idStreamer,
            @Valid @RequestBody AsignacionRequestDTO dto) {
        log.info("POST /streamers/{}/coberturas - Asignando cobertura (partido ID: {})",
                idStreamer);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(streamerService.asignarCobertura(idStreamer, dto));
    }
}
