package com.esports.mspatrocinadores.controller;

import com.esports.mspatrocinadores.dto.*;
import com.esports.mspatrocinadores.service.PatrocinadorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patrocinadores")
@RequiredArgsConstructor
public class PatrocinadorController {

    private static final Logger log = LoggerFactory.getLogger(PatrocinadorController.class);

    private final PatrocinadorService patrocinadorService;

    @PostMapping
    public ResponseEntity<PatrocinadorResponseDTO> crear(@Valid @RequestBody PatrocinadorRequestDTO dto) {
        log.info("POST /patrocinadores - Creando patrocinador: {}");
        return ResponseEntity.status(HttpStatus.CREATED).body(patrocinadorService.crear(dto));
    }

    @GetMapping
    public ResponseEntity<List<PatrocinadorResponseDTO>> listar() {
        log.debug("GET /patrocinadores - Listando todos los patrocinadores");
        return ResponseEntity.ok(patrocinadorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatrocinadorResponseDTO> buscarPorId(@PathVariable Long id) {
        log.info("GET /patrocinadores/{} - Buscando patrocinador por ID", id);
        return ResponseEntity.ok(patrocinadorService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<PatrocinadorResponseDTO>> buscarPorTier(@RequestParam String tier) {
        log.info("GET /patrocinadores/buscar?tier={} - Buscando patrocinadores por tier", tier);
        return ResponseEntity.ok(patrocinadorService.buscarPorTier(tier));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatrocinadorResponseDTO> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody PatrocinadorRequestDTO dto) {
        log.info("PUT /patrocinadores/{} - Actualizando patrocinador: {}", id);
        return ResponseEntity.ok(patrocinadorService.actualizar(id, dto));
    }

    @PatchMapping("/{id}/desactivar")
    public ResponseEntity<Void> desactivar(@PathVariable Long id) {
        log.warn("PATCH /patrocinadores/{}/desactivar - Desactivando patrocinador", id);
        patrocinadorService.desactivar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /patrocinadores/{} - Eliminando patrocinador (destructivo)", id);
        patrocinadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{idPatrocinador}/contratos")
    public ResponseEntity<ContratoResponseDTO> agregarContrato(
            @PathVariable Long idPatrocinador,
            @Valid @RequestBody ContratoRequestDTO dto) {
        log.info("POST /patrocinadores/{}/contratos - Agregando contrato con monto: {}",
                idPatrocinador);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(patrocinadorService.agregarContrato(idPatrocinador, dto));
    }
}
