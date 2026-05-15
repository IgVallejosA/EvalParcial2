package com.esports.msestadisticas.service;

import com.esports.msestadisticas.client.*;
import com.esports.msestadisticas.dto.*;
import com.esports.msestadisticas.model.Estadistica;
import com.esports.msestadisticas.exception.RecursoNoEncontradoException;
import com.esports.msestadisticas.exception.ReglaNegocioException;
import com.esports.msestadisticas.repository.EstadisticaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstadisticaService {

    private static final Logger log = LoggerFactory.getLogger(EstadisticaService.class);

    private final EstadisticaRepository estadisticaRepository;
    private final JugadorClient jugadorClient;
    private final PartidaClient partidaClient;

    @Transactional
    public EstadisticaResponseDTO registrar(EstadisticaRequestDTO dto) {
        log.info("Registrando estadística: jugador={}, partida={}", dto.getIdJugador(), dto.getIdPartida());
        try {

            if (estadisticaRepository.existsByIdJugadorAndIdPartida(dto.getIdJugador(), dto.getIdPartida())) {
                throw new ReglaNegocioException(
                        "Ya existe estadística para el jugador " + dto.getIdJugador()
                                + " en la partida " + dto.getIdPartida());
            }

            JugadorRemotoDTO jugador = jugadorClient.obtenerJugadorPorId(dto.getIdJugador());
            PartidaRemotoDTO partida = partidaClient.obtenerPartidaPorId(dto.getIdPartida());

            if (!"FINALIZADA".equals(partida.getEstado())) {
                throw new ReglaNegocioException(
                        "Solo se pueden registrar stats en partidas finalizadas. Estado actual: "
                                + partida.getEstado());
            }

            double kda = calcularKDA(dto.getKills(), dto.getDeaths(), dto.getAssists());

            Estadistica est = Estadistica.builder()
                    .idJugador(dto.getIdJugador())
                    .nicknameJugador(jugador.getNickname())
                    .idPartida(dto.getIdPartida())
                    .kills(dto.getKills())
                    .deaths(dto.getDeaths())
                    .assists(dto.getAssists())
                    .kda(kda)
                    .damageDealt(dto.getDamageDealt())
                    .tiempoJugadoMinutos(dto.getTiempoJugadoMinutos())
                    .mvp(dto.getMvp())
                    .fechaRegistro(LocalDateTime.now())
                    .build();

            Estadistica guardada = estadisticaRepository.save(est);
            log.info("Estadística registrada - ID: {}, KDA: {}", guardada.getId(), kda);
            return mapearAResponse(guardada);

        } catch (ReglaNegocioException | RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al registrar estadística: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al registrar estadística");
        }
    }

    @Transactional(readOnly = true)
    public List<EstadisticaResponseDTO> listar() {
        return estadisticaRepository.findAll().stream().map(this::mapearAResponse).toList();
    }

    @Transactional(readOnly = true)
    public EstadisticaResponseDTO buscarPorId(Long id) {
        return mapearAResponse(estadisticaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estadística no encontrada: " + id)));
    }

    @Transactional(readOnly = true)
    public List<EstadisticaResponseDTO> buscarPorJugador(Long idJugador) {
        return estadisticaRepository.findByIdJugador(idJugador).stream()
                .map(this::mapearAResponse).toList();
    }

    @Transactional(readOnly = true)
    public List<EstadisticaResponseDTO> buscarPorPartida(Long idPartida) {
        return estadisticaRepository.findByIdPartida(idPartida).stream()
                .map(this::mapearAResponse).toList();
    }

    @Transactional(readOnly = true)
    public ResumenJugadorDTO obtenerResumenJugador(Long idJugador) {
        log.info("Generando resumen de jugador {}", idJugador);
        List<Estadistica> stats = estadisticaRepository.findByIdJugador(idJugador);

        if (stats.isEmpty()) {
            throw new RecursoNoEncontradoException(
                    "No hay estadísticas registradas para el jugador " + idJugador);
        }

        int totalK = stats.stream().mapToInt(Estadistica::getKills).sum();
        int totalD = stats.stream().mapToInt(Estadistica::getDeaths).sum();
        int totalA = stats.stream().mapToInt(Estadistica::getAssists).sum();
        double kdaPromedio = stats.stream().mapToDouble(Estadistica::getKda).average().orElse(0.0);
        int mvps = (int) stats.stream().filter(Estadistica::getMvp).count();

        return ResumenJugadorDTO.builder()
                .idJugador(idJugador)
                .nicknameJugador(stats.get(0).getNicknameJugador())
                .partidasJugadas(stats.size())
                .totalKills(totalK)
                .totalDeaths(totalD)
                .totalAssists(totalA)
                .kdaPromedio(Math.round(kdaPromedio * 100.0) / 100.0)
                .mvps(mvps)
                .build();
    }

    @Transactional
    public void eliminar(Long id) {
        if (!estadisticaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Estadística no encontrada: " + id);
        }
        estadisticaRepository.deleteById(id);
    }

    private double calcularKDA(int k, int d, int a) {
        return Math.round(((double) (k + a) / Math.max(d, 1)) * 100.0) / 100.0;
    }

    private EstadisticaResponseDTO mapearAResponse(Estadistica e) {
        return EstadisticaResponseDTO.builder()
                .id(e.getId())
                .idJugador(e.getIdJugador())
                .nicknameJugador(e.getNicknameJugador())
                .idPartida(e.getIdPartida())
                .kills(e.getKills())
                .deaths(e.getDeaths())
                .assists(e.getAssists())
                .kda(e.getKda())
                .damageDealt(e.getDamageDealt())
                .tiempoJugadoMinutos(e.getTiempoJugadoMinutos())
                .mvp(e.getMvp())
                .fechaRegistro(e.getFechaRegistro())
                .build();
    }
}
