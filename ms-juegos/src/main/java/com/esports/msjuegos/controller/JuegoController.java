package com.esports.msjuegos.controller;

import com.esports.msjuegos.dto.*;
import com.esports.msjuegos.service.JuegoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/juegos")
@RequiredArgsConstructor
public class JuegoController {

    private static final Logger log = LoggerFactory.getLogger(JuegoController.class);

    private final JuegoService juegoService;

    @PostMapping
    public ResponseEntity<JuegoResponseDTO> crear(@Valid @RequestBody JuegoRequestDTO dto) {
        log.debug("POST /api/v1/juegos - {}", dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(juegoService.crearJuego(dto));
    }

    @GetMapping
    public ResponseEntity<List<JuegoResponseDTO>> listar() {
        log.debug("GET /api/v1/juegos");
        return ResponseEntity.ok(juegoService.listarJuegos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JuegoResponseDTO> buscarPorId(@PathVariable Long id) {
        log.debug("GET /api/v1/juegos/{}", id);
        return ResponseEntity.ok(juegoService.buscarJuegoPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<JuegoResponseDTO>> buscarPorGenero(@RequestParam String genero) {
        log.debug("GET /api/v1/juegos/buscar?genero={}", genero);
        return ResponseEntity.ok(juegoService.buscarPorGenero(genero));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JuegoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody JuegoRequestDTO dto) {
        log.debug("PUT /api/v1/juegos/{}", id);
        return ResponseEntity.ok(juegoService.actualizarJuego(id, dto));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        log.debug("PATCH /api/v1/juegos/{}/desactivar", id);
        juegoService.desactivarJuego(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.debug("DELETE /api/v1/juegos/{}", id);
        juegoService.eliminarJuego(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idJuego}/modos")
    public ResponseEntity<ModoCompetitivoResponseDTO> agregarModo(
            @PathVariable Long idJuego,
            @Valid @RequestBody ModoCompetitivoRequestDTO dto) {
        log.debug("POST /api/v1/juegos/{}/modos - {}", idJuego, dto.getNombreModo());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(juegoService.agregarModo(idJuego, dto));
    }

    @GetMapping("/{idJuego}/modos")
    public ResponseEntity<List<ModoCompetitivoResponseDTO>> listarModos(@PathVariable Long idJuego) {
        log.debug("GET /api/v1/juegos/{}/modos", idJuego);
        return ResponseEntity.ok(juegoService.listarModosDeJuego(idJuego));
    }
}
