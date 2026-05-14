package com.esports.msrankings.service;

import com.esports.msrankings.client.*;
import com.esports.msrankings.dto.*;
import com.esports.msrankings.model.RankingEntrada;
import com.esports.msrankings.exception.RecursoNoEncontradoException;
import com.esports.msrankings.exception.ReglaNegocioException;
import com.esports.msrankings.repository.RankingEntradaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RankingService {

    private static final Logger log = LoggerFactory.getLogger(RankingService.class);

    private final RankingEntradaRepository rankingRepository;
    private final EquipoClient equipoClient;
    private final JugadorClient jugadorClient;

    @Transactional
    public RankingResponseDTO crearOActualizar(RankingRequestDTO dto) {
        log.info("Creando/actualizando ranking: {} ID={} en region={}",
                dto.getTipoEntidad(), dto.getIdEntidad(), dto.getRegion());

        try {

            String nombreEntidad;
            if ("EQUIPO".equals(dto.getTipoEntidad())) {
                EquipoRemotoDTO e = equipoClient.obtenerEquipoPorId(dto.getIdEntidad());
                nombreEntidad = e.getNombre();
            } else { 
                JugadorRemotoDTO j = jugadorClient.obtenerJugadorPorId(dto.getIdEntidad());
                nombreEntidad = j.getNickname();
            }

            RankingEntrada entrada = rankingRepository
                    .findByTipoEntidadAndIdEntidadAndRegion(
                            dto.getTipoEntidad(), dto.getIdEntidad(), dto.getRegion())
                    .orElseGet(() -> RankingEntrada.builder()
                            .tipoEntidad(dto.getTipoEntidad())
                            .idEntidad(dto.getIdEntidad())
                            .region(dto.getRegion())
                            .build());

            entrada.setNombreEntidad(nombreEntidad);
            entrada.setPuntos(dto.getPuntos());
            entrada.setVictorias(dto.getVictorias() != null ? dto.getVictorias() : 0);
            entrada.setDerrotas(dto.getDerrotas() != null ? dto.getDerrotas() : 0);
            entrada.setFechaActualizacion(LocalDateTime.now());

            RankingEntrada guardada = rankingRepository.save(entrada);

            recalcularPosiciones(dto.getTipoEntidad(), dto.getRegion());

            RankingEntrada actualizada = rankingRepository.findById(guardada.getId()).get();
            log.info("Ranking actualizado - posición: {}", actualizada.getPosicion());
            return mapearAResponse(actualizada);

        } catch (RecursoNoEncontradoException | ReglaNegocioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar ranking: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al actualizar ranking");
        }
    }

    private void recalcularPosiciones(String tipoEntidad, String region) {
        log.debug("Recalculando posiciones: {} en {}", tipoEntidad, region);
        List<RankingEntrada> ordenado = rankingRepository
                .findByTipoEntidadAndRegionOrderByPuntosDesc(tipoEntidad, region);

        for (int i = 0; i < ordenado.size(); i++) {
            ordenado.get(i).setPosicion(i + 1);
        }
        rankingRepository.saveAll(ordenado);
    }

    @Transactional(readOnly = true)
    public List<RankingResponseDTO> obtenerRanking(String tipoEntidad, String region) {
        log.info("Obteniendo ranking de {} en region {}", tipoEntidad, region);
        return rankingRepository
                .findByTipoEntidadAndRegionOrderByPuntosDesc(tipoEntidad, region)
                .stream()
                .map(this::mapearAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RankingResponseDTO> listar() {
        return rankingRepository.findAll().stream().map(this::mapearAResponse).toList();
    }

    @Transactional(readOnly = true)
    public RankingResponseDTO buscarPorId(Long id) {
        return mapearAResponse(rankingRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Entrada no encontrada: " + id)));
    }

    @Transactional
    public void eliminar(Long id) {
        RankingEntrada r = rankingRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Entrada no encontrada: " + id));
        String tipo = r.getTipoEntidad();
        String region = r.getRegion();
        rankingRepository.deleteById(id);
        recalcularPosiciones(tipo, region); 
    }

    private RankingResponseDTO mapearAResponse(RankingEntrada r) {
        Double winRate = null;
        int v = r.getVictorias() != null ? r.getVictorias() : 0;
        int d = r.getDerrotas() != null ? r.getDerrotas() : 0;
        if (v + d > 0) {
            winRate = Math.round(((double) v / (v + d)) * 10000.0) / 100.0;
        }

        return RankingResponseDTO.builder()
                .id(r.getId())
                .tipoEntidad(r.getTipoEntidad())
                .idEntidad(r.getIdEntidad())
                .nombreEntidad(r.getNombreEntidad())
                .region(r.getRegion())
                .puntos(r.getPuntos())
                .posicion(r.getPosicion())
                .victorias(r.getVictorias())
                .derrotas(r.getDerrotas())
                .winRate(winRate)
                .fechaActualizacion(r.getFechaActualizacion())
                .build();
    }
}
