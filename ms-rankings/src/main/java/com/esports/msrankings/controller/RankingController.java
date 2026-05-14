package com.esports.msrankings.controller;

import com.esports.msrankings.dto.*;
import com.esports.msrankings.service.RankingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rankings")
@RequiredArgsConstructor
public class RankingController {

    private static final Logger log = LoggerFactory.getLogger(RankingController.class);

    private final RankingService rankingService;

    @PostMapping
    public ResponseEntity<RankingResponseDTO> crearOActualizar(@Valid @RequestBody RankingRequestDTO dto) {
        log.info("POST /rankings - Creando/actualizando ranking para tipo: {}", dto.getTipoEntidad());
        return ResponseEntity.status(HttpStatus.CREATED).body(rankingService.crearOActualizar(dto));
    }

    @GetMapping
    public ResponseEntity<List<RankingResponseDTO>> listar() {
        log.debug("GET /rankings - Listando todos los rankings");
        return ResponseEntity.ok(rankingService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RankingResponseDTO> buscarPorId(@PathVariable Long id) {
        log.info("GET /rankings/{} - Buscando ranking por ID", id);
        return ResponseEntity.ok(rankingService.buscarPorId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<RankingResponseDTO>> obtenerRanking(
            @RequestParam String tipoEntidad,
            @RequestParam String region) {
        log.info("GET /rankings/buscar?tipoEntidad={}&region={} - Consultando ranking", tipoEntidad, region);
        return ResponseEntity.ok(rankingService.obtenerRanking(tipoEntidad, region));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        log.warn("DELETE /rankings/{} - Eliminando ranking", id);
        rankingService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
