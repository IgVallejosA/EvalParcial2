package com.esports.msequipos.controller;

import com.esports.msequipos.dto.*;
import com.esports.msequipos.service.EquipoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/equipos")
@RequiredArgsConstructor
public class EquipoController {

    private static final Logger log = LoggerFactory.getLogger(EquipoController.class);

    private final EquipoService equipoService;

    // ==================== EQUIPOS ====================

    @PostMapping
    public ResponseEntity<EquipoResponseDTO> crear(@Valid @RequestBody EquipoRequestDTO dto) {
        log.debug("POST /api/v1/equipos - {}", dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(equipoService.crearEquipo(dto));
    }

    @GetMapping
    public ResponseEntity<List<EquipoResponseDTO>> listar() {
        log.debug("GET /api/v1/equipos");
        return ResponseEntity.ok(equipoService.listarEquipos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipoResponseDTO> buscarPorId(@PathVariable Long id) {
        log.debug("GET /api/v1/equipos/{}", id);
        return ResponseEntity.ok(equipoService.buscarEquipoPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<EquipoResponseDTO>> buscarPorRegion(@RequestParam String region) {
        log.debug("GET /api/v1/equipos/buscar?region={}", region);
        return ResponseEntity.ok(equipoService.buscarPorRegion(region));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipoResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody EquipoRequestDTO dto) {
        log.debug("PUT /api/v1/equipos/{}", id);
        return ResponseEntity.ok(equipoService.actualizarEquipo(id, dto));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        log.debug("PATCH /api/v1/equipos/{}/desactivar", id);
        equipoService.desactivarEquipo(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.debug("DELETE /api/v1/equipos/{}", id);
        equipoService.eliminarEquipo(id);
        return ResponseEntity.noContent().build();
    }

    // ==================== STAFF ====================

    @PostMapping("/{idEquipo}/staff")
    public ResponseEntity<StaffResponseDTO> agregarStaff(
            @PathVariable Long idEquipo,
            @Valid @RequestBody StaffRequestDTO dto) {
        log.debug("POST /api/v1/equipos/{}/staff - {}", idEquipo, dto.getNombre());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(equipoService.agregarStaff(idEquipo, dto));
    }

    // ==================== ROSTER ====================

    @PostMapping("/{idEquipo}/roster")
    public ResponseEntity<RosterResponseDTO> agregarJugador(
            @PathVariable Long idEquipo,
            @Valid @RequestBody RosterRequestDTO dto) {
        log.debug("POST /api/v1/equipos/{}/roster - jugadorId: {}", idEquipo, dto.getIdJugador());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(equipoService.agregarJugadorAlRoster(idEquipo, dto));
    }
}
