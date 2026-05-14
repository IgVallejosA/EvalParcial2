package com.esports.mspartidas.service;

import com.esports.mspartidas.client.*;
import com.esports.mspartidas.dto.*;
import com.esports.mspartidas.model.Partida;
import com.esports.mspartidas.exception.RecursoNoEncontradoException;
import com.esports.mspartidas.exception.ReglaNegocioException;
import com.esports.mspartidas.repository.PartidaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PartidaService {

    private static final Logger log = LoggerFactory.getLogger(PartidaService.class);

    private final PartidaRepository partidaRepository;
    private final TorneoClient torneoClient;
    private final EquipoClient equipoClient;

    @Transactional
    public PartidaResponseDTO programarPartida(PartidaRequestDTO dto) {
        log.info("Programando partida: torneo={}, local={}, visitante={}",
                dto.getIdTorneo(), dto.getIdEquipoLocal(), dto.getIdEquipoVisitante());

        try {

            if (dto.getIdEquipoLocal().equals(dto.getIdEquipoVisitante())) {
                throw new ReglaNegocioException("Un equipo no puede jugar contra sí mismo");
            }

            TorneoRemotoDTO torneo = torneoClient.obtenerTorneoPorId(dto.getIdTorneo());
            if ("FINALIZADO".equals(torneo.getEstado()) || "CANCELADO".equals(torneo.getEstado())) {
                throw new ReglaNegocioException("No se pueden programar partidas en torneo " + torneo.getEstado());
            }

            EquipoRemotoDTO local = equipoClient.obtenerEquipoPorId(dto.getIdEquipoLocal());
            EquipoRemotoDTO visitante = equipoClient.obtenerEquipoPorId(dto.getIdEquipoVisitante());

            if (Boolean.FALSE.equals(local.getActivo()) || Boolean.FALSE.equals(visitante.getActivo())) {
                throw new ReglaNegocioException("Ambos equipos deben estar activos para programar la partida");
            }

            Partida partida = Partida.builder()
                    .idTorneo(dto.getIdTorneo())
                    .nombreTorneo(torneo.getNombre())
                    .idEquipoLocal(dto.getIdEquipoLocal())
                    .nombreEquipoLocal(local.getNombre())
                    .idEquipoVisitante(dto.getIdEquipoVisitante())
                    .nombreEquipoVisitante(visitante.getNombre())
                    .fechaHora(dto.getFechaHora())
                    .mapaOEscenario(dto.getMapaOEscenario())
                    .estado("PROGRAMADA")
                    .build();

            Partida guardada = partidaRepository.save(partida);
            log.info("Partida programada - ID: {}", guardada.getId());
            return mapearAResponse(guardada);

        } catch (ReglaNegocioException | RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al programar partida: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al programar partida");
        }
    }

    @Transactional
    public PartidaResponseDTO registrarResultado(Long idPartida, ResultadoRequestDTO dto) {
        log.info("Registrando resultado de partida {}", idPartida);
        Partida partida = obtenerOFallar(idPartida);

        if ("FINALIZADA".equals(partida.getEstado())) {
            throw new ReglaNegocioException("No se puede modificar el resultado de una partida finalizada");
        }
        if ("CANCELADA".equals(partida.getEstado())) {
            throw new ReglaNegocioException("No se puede registrar resultado en partida cancelada");
        }

        partida.setMarcadorLocal(dto.getMarcadorLocal());
        partida.setMarcadorVisitante(dto.getMarcadorVisitante());
        partida.setDuracionMinutos(dto.getDuracionMinutos());

        if (dto.getMarcadorLocal() > dto.getMarcadorVisitante()) {
            partida.setIdEquipoGanador(partida.getIdEquipoLocal());
        } else if (dto.getMarcadorVisitante() > dto.getMarcadorLocal()) {
            partida.setIdEquipoGanador(partida.getIdEquipoVisitante());
        } else {
            partida.setIdEquipoGanador(null); 
            log.info("Partida {} terminó en empate", idPartida);
        }

        partida.setEstado("FINALIZADA");
        Partida actualizada = partidaRepository.save(partida);
        log.info("Resultado registrado: {} - {} a {}",
                actualizada.getId(), actualizada.getMarcadorLocal(), actualizada.getMarcadorVisitante());

        return mapearAResponse(actualizada);
    }

    @Transactional
    public PartidaResponseDTO cancelar(Long id) {
        Partida p = obtenerOFallar(id);
        if ("FINALIZADA".equals(p.getEstado())) {
            throw new ReglaNegocioException("No se puede cancelar una partida finalizada");
        }
        p.setEstado("CANCELADA");
        return mapearAResponse(partidaRepository.save(p));
    }

    @Transactional(readOnly = true)
    public List<PartidaResponseDTO> listar() {
        return partidaRepository.findAll().stream().map(this::mapearAResponse).toList();
    }

    @Transactional(readOnly = true)
    public PartidaResponseDTO buscarPorId(Long id) {
        return mapearAResponse(obtenerOFallar(id));
    }

    @Transactional(readOnly = true)
    public List<PartidaResponseDTO> buscarPorTorneo(Long idTorneo) {
        return partidaRepository.findByIdTorneo(idTorneo).stream().map(this::mapearAResponse).toList();
    }

    @Transactional
    public void eliminar(Long id) {
        if (!partidaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Partida no encontrada: " + id);
        }
        partidaRepository.deleteById(id);
    }

    private Partida obtenerOFallar(Long id) {
        return partidaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Partida no encontrada: " + id));
    }

    private PartidaResponseDTO mapearAResponse(Partida p) {
        return PartidaResponseDTO.builder()
                .id(p.getId())
                .idTorneo(p.getIdTorneo())
                .nombreTorneo(p.getNombreTorneo())
                .idEquipoLocal(p.getIdEquipoLocal())
                .nombreEquipoLocal(p.getNombreEquipoLocal())
                .idEquipoVisitante(p.getIdEquipoVisitante())
                .nombreEquipoVisitante(p.getNombreEquipoVisitante())
                .fechaHora(p.getFechaHora())
                .duracionMinutos(p.getDuracionMinutos())
                .marcadorLocal(p.getMarcadorLocal())
                .marcadorVisitante(p.getMarcadorVisitante())
                .idEquipoGanador(p.getIdEquipoGanador())
                .estado(p.getEstado())
                .mapaOEscenario(p.getMapaOEscenario())
                .build();
    }
}
