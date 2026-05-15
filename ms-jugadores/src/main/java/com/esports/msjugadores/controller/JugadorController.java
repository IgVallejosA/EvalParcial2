package com.esports.msjugadores.controller;

import com.esports.msjugadores.dto.JugadorRequestDTO;
import com.esports.msjugadores.dto.JugadorResponseDTO;
import com.esports.msjugadores.service.JugadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jugadores")
@RequiredArgsConstructor
public class JugadorController {

    private static final Logger log = LoggerFactory.getLogger(JugadorController.class);

    private final JugadorService jugadorService;

    @PostMapping
    public ResponseEntity<JugadorResponseDTO> crear(@Valid @RequestBody JugadorRequestDTO dto) {
        log.debug("POST /api/v1/jugadores - nickname: {}", dto.getNickname());
        JugadorResponseDTO creado = jugadorService.crear(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @GetMapping
    public ResponseEntity<List<JugadorResponseDTO>> listar() {
        log.debug("GET /api/v1/jugadores");
        return ResponseEntity.ok(jugadorService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<JugadorResponseDTO> buscarPorId(@PathVariable Long id) {
        log.debug("GET /api/v1/jugadores/{}", id);
        return ResponseEntity.ok(jugadorService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<JugadorResponseDTO>> buscarPorPais(@RequestParam String pais) {
        log.debug("GET /api/v1/jugadores/buscar?pais={}", pais);
        return ResponseEntity.ok(jugadorService.buscarPorPais(pais));
    }

    @PutMapping("/{id}")
    public ResponseEntity<JugadorResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody JugadorRequestDTO dto) {
        log.debug("PUT /api/v1/jugadores/{}", id);
        return ResponseEntity.ok(jugadorService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        log.debug("PATCH /api/v1/jugadores/{}/desactivar", id);
        jugadorService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.debug("DELETE /api/v1/jugadores/{}", id);
        jugadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
