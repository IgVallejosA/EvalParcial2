package com.esports.mstransferencias.service;

import com.esports.mstransferencias.client.*;
import com.esports.mstransferencias.dto.*;
import com.esports.mstransferencias.model.Transferencia;
import com.esports.mstransferencias.exception.RecursoNoEncontradoException;
import com.esports.mstransferencias.exception.ReglaNegocioException;
import com.esports.mstransferencias.repository.TransferenciaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransferenciaService {

    private static final Logger log = LoggerFactory.getLogger(TransferenciaService.class);

    private final TransferenciaRepository transferenciaRepository;
    private final JugadorClient jugadorClient;
    private final EquipoClient equipoClient;

    @Transactional
    public TransferenciaResponseDTO registrar(TransferenciaRequestDTO dto) {
        log.info("Registrando transferencia de jugador {} ({})", dto.getIdJugador(), dto.getTipo());
        try {
            validarTipoTransferencia(dto);

            if (dto.getIdEquipoOrigen() != null
                    && dto.getIdEquipoOrigen().equals(dto.getIdEquipoDestino())) {
                throw new ReglaNegocioException("Equipo origen y destino no pueden ser el mismo");
            }

            JugadorRemotoDTO jugador = jugadorClient.obtenerJugadorPorId(dto.getIdJugador());

            String nombreOrigen = null;
            String nombreDestino = null;

            if (dto.getIdEquipoOrigen() != null) {
                EquipoRemotoDTO origen = equipoClient.obtenerEquipoPorId(dto.getIdEquipoOrigen());
                nombreOrigen = origen.getNombre();
            }
            if (dto.getIdEquipoDestino() != null) {
                EquipoRemotoDTO destino = equipoClient.obtenerEquipoPorId(dto.getIdEquipoDestino());
                if (Boolean.FALSE.equals(destino.getActivo())) {
                    throw new ReglaNegocioException(
                            "No se puede transferir a equipo inactivo: " + destino.getNombre());
                }
                nombreDestino = destino.getNombre();
            }

            Transferencia t = Transferencia.builder()
                    .idJugador(dto.getIdJugador())
                    .nicknameJugador(jugador.getNickname())
                    .idEquipoOrigen(dto.getIdEquipoOrigen())
                    .nombreEquipoOrigen(nombreOrigen)
                    .idEquipoDestino(dto.getIdEquipoDestino())
                    .nombreEquipoDestino(nombreDestino)
                    .fechaTransferencia(dto.getFechaTransferencia())
                    .montoUsd(dto.getMontoUsd())
                    .tipo(dto.getTipo())
                    .duracionContratoMeses(dto.getDuracionContratoMeses())
                    .observaciones(dto.getObservaciones())
                    .build();

            Transferencia guardada = transferenciaRepository.save(t);
            log.info("Transferencia registrada - ID: {} | jugador: {} | tipo: {}",
                    guardada.getId(), jugador.getNickname(), dto.getTipo());

            return mapearAResponse(guardada);

        } catch (ReglaNegocioException | RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al registrar transferencia: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al registrar transferencia");
        }
    }

    private void validarTipoTransferencia(TransferenciaRequestDTO dto) {
        switch (dto.getTipo()) {
            case "FICHAJE_INICIAL":
                if (dto.getIdEquipoOrigen() != null) {
                    throw new ReglaNegocioException(
                            "FICHAJE_INICIAL no debe tener equipo origen (jugador es novato)");
                }
                if (dto.getIdEquipoDestino() == null) {
                    throw new ReglaNegocioException(
                            "FICHAJE_INICIAL requiere equipo destino");
                }
                break;
            case "BAJA":
                if (dto.getIdEquipoDestino() != null) {
                    throw new ReglaNegocioException(
                            "BAJA no debe tener equipo destino (jugador queda libre)");
                }
                if (dto.getIdEquipoOrigen() == null) {
                    throw new ReglaNegocioException(
                            "BAJA requiere equipo origen");
                }
                break;
            case "TRANSFERENCIA":
            case "PRESTAMO":
                if (dto.getIdEquipoOrigen() == null || dto.getIdEquipoDestino() == null) {
                    throw new ReglaNegocioException(
                            dto.getTipo() + " requiere ambos equipos (origen y destino)");
                }
                break;
        }
    }

    @Transactional(readOnly = true)
    public List<TransferenciaResponseDTO> listar() {
        return transferenciaRepository.findAll().stream().map(this::mapearAResponse).toList();
    }

    @Transactional(readOnly = true)
    public TransferenciaResponseDTO buscarPorId(Long id) {
        return mapearAResponse(transferenciaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Transferencia no encontrada: " + id)));
    }

    @Transactional(readOnly = true)
    public List<TransferenciaResponseDTO> historialJugador(Long idJugador) {
        log.info("Consultando historial del jugador {}", idJugador);
        return transferenciaRepository
                .findByIdJugadorOrderByFechaTransferenciaDesc(idJugador)
                .stream()
                .map(this::mapearAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TransferenciaResponseDTO> buscarPorTipo(String tipo) {
        return transferenciaRepository.findByTipo(tipo).stream().map(this::mapearAResponse).toList();
    }

    @Transactional
    public void eliminar(Long id) {
        if (!transferenciaRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Transferencia no encontrada: " + id);
        }
        transferenciaRepository.deleteById(id);
        log.warn("Transferencia {} eliminada", id);
    }

    private TransferenciaResponseDTO mapearAResponse(Transferencia t) {
        return TransferenciaResponseDTO.builder()
                .id(t.getId())
                .idJugador(t.getIdJugador())
                .nicknameJugador(t.getNicknameJugador())
                .idEquipoOrigen(t.getIdEquipoOrigen())
                .nombreEquipoOrigen(t.getNombreEquipoOrigen())
                .idEquipoDestino(t.getIdEquipoDestino())
                .nombreEquipoDestino(t.getNombreEquipoDestino())
                .fechaTransferencia(t.getFechaTransferencia())
                .montoUsd(t.getMontoUsd())
                .tipo(t.getTipo())
                .duracionContratoMeses(t.getDuracionContratoMeses())
                .observaciones(t.getObservaciones())
                .build();
    }
}
