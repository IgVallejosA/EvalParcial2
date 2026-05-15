package com.esports.msequipos.service;

import com.esports.msequipos.client.JugadorClient;
import com.esports.msequipos.client.JugadorRemotoDTO;
import com.esports.msequipos.dto.*;
import com.esports.msequipos.model.Equipo;
import com.esports.msequipos.model.RosterHistorico;
import com.esports.msequipos.model.StaffTecnico;
import com.esports.msequipos.exception.RecursoNoEncontradoException;
import com.esports.msequipos.exception.ReglaNegocioException;
import com.esports.msequipos.repository.EquipoRepository;
import com.esports.msequipos.repository.RosterHistoricoRepository;
import com.esports.msequipos.repository.StaffTecnicoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EquipoService {

    private static final Logger log = LoggerFactory.getLogger(EquipoService.class);

    private final EquipoRepository equipoRepository;
    private final StaffTecnicoRepository staffRepository;
    private final RosterHistoricoRepository rosterRepository;
    private final JugadorClient jugadorClient;

    private static final String ROL_HEAD_COACH = "HEAD_COACH";

    @Transactional
    public EquipoResponseDTO crearEquipo(EquipoRequestDTO dto) {
        log.info("Creando equipo: {} ({})", dto.getNombre(), dto.getRegion());

        try {
            if (equipoRepository.existsByNombreAndRegion(dto.getNombre(), dto.getRegion())) {
                log.warn("Equipo duplicado rechazado: {} en región {}", dto.getNombre(), dto.getRegion());
                throw new ReglaNegocioException(
                        "Ya existe un equipo con el nombre '" + dto.getNombre()
                                + "' en la región " + dto.getRegion());
            }

            Equipo equipo = Equipo.builder()
                    .nombre(dto.getNombre())
                    .region(dto.getRegion())
                    .fechaFundacion(dto.getFechaFundacion())
                    .rankingMundial(dto.getRankingMundial())
                    .presupuestoAnualUsd(dto.getPresupuestoAnualUsd())
                    .activo(true)
                    .build();

            Equipo guardado = equipoRepository.save(equipo);
            log.info("Equipo creado - ID: {}", guardado.getId());

            return mapearEquipoAResponse(guardado);

        } catch (ReglaNegocioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al crear equipo: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al crear el equipo: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<EquipoResponseDTO> listarEquipos() {
        log.info("Listando todos los equipos");
        return equipoRepository.findAll().stream()
                .map(this::mapearEquipoAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EquipoResponseDTO buscarEquipoPorId(Long id) {
        log.info("Buscando equipo con ID: {}", id);
        Equipo equipo = obtenerEquipoOFallar(id);
        return mapearEquipoAResponse(equipo);
    }

    @Transactional(readOnly = true)
    public List<EquipoResponseDTO> buscarPorRegion(String region) {
        log.info("Buscando equipos de la región: {}", region);
        return equipoRepository.findByRegion(region).stream()
                .map(this::mapearEquipoAResponse)
                .toList();
    }

    @Transactional
    public EquipoResponseDTO actualizarEquipo(Long id, EquipoRequestDTO dto) {
        log.info("Actualizando equipo ID: {}", id);
        try {
            Equipo equipo = obtenerEquipoOFallar(id);

            if ((!equipo.getNombre().equals(dto.getNombre())
                    || !equipo.getRegion().equals(dto.getRegion()))
                    && equipoRepository.existsByNombreAndRegion(dto.getNombre(), dto.getRegion())) {
                throw new ReglaNegocioException(
                        "Ya existe otro equipo con ese nombre en esa región");
            }

            equipo.setNombre(dto.getNombre());
            equipo.setRegion(dto.getRegion());
            equipo.setFechaFundacion(dto.getFechaFundacion());
            equipo.setRankingMundial(dto.getRankingMundial());
            equipo.setPresupuestoAnualUsd(dto.getPresupuestoAnualUsd());

            Equipo actualizado = equipoRepository.save(equipo);
            log.info("Equipo actualizado correctamente: {}", actualizado.getId());
            return mapearEquipoAResponse(actualizado);

        } catch (RecursoNoEncontradoException | ReglaNegocioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar equipo {}: {}", id, e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al actualizar el equipo");
        }
    }

    @Transactional
    public void desactivarEquipo(Long id) {
        log.info("Desactivando equipo ID: {}", id);
        Equipo equipo = obtenerEquipoOFallar(id);

        if (!equipo.getActivo()) {
            throw new ReglaNegocioException("El equipo ya está desactivado");
        }

        List<RosterHistorico> activos = rosterRepository.findByEquipoIdAndFechaFinIsNull(id);
        if (!activos.isEmpty()) {
            log.warn("No se puede desactivar equipo {} - tiene {} jugadores activos",
                    id, activos.size());
            throw new ReglaNegocioException(
                    "No se puede desactivar el equipo. Tiene " + activos.size()
                            + " jugadores activos en el roster. Cierre sus contratos primero.");
        }

        equipo.setActivo(false);
        equipoRepository.save(equipo);
        log.info("Equipo {} desactivado correctamente", id);
    }

    @Transactional
    public void eliminarEquipo(Long id) {
        log.info("Eliminando físicamente equipo ID: {}", id);
        if (!equipoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Equipo no encontrado con ID: " + id);
        }
        equipoRepository.deleteById(id);
        log.warn("Equipo {} eliminado FÍSICAMENTE", id);
    }

    @Transactional
    public StaffResponseDTO agregarStaff(Long idEquipo, StaffRequestDTO dto) {
        log.info("Agregando staff '{}' (rol: {}) al equipo {}",
                dto.getNombre(), dto.getRolStaff(), idEquipo);
        try {
            Equipo equipo = obtenerEquipoOFallar(idEquipo);

            if (ROL_HEAD_COACH.equals(dto.getRolStaff())) {
                List<StaffTecnico> headCoachesActivos = staffRepository
                        .findByEquipoIdAndRolStaffAndActivoTrue(idEquipo, ROL_HEAD_COACH);
                if (!headCoachesActivos.isEmpty()) {
                    log.warn("Rechazado: el equipo {} ya tiene un Head Coach activo", idEquipo);
                    throw new ReglaNegocioException(
                            "El equipo ya tiene un Head Coach activo. " +
                                    "Desactive al actual antes de asignar uno nuevo.");
                }
            }

            StaffTecnico staff = StaffTecnico.builder()
                    .nombre(dto.getNombre())
                    .rolStaff(dto.getRolStaff())
                    .salarioMensualUsd(dto.getSalarioMensualUsd())
                    .activo(true)
                    .build();

            equipo.agregarStaff(staff);
            equipoRepository.save(equipo);

            log.info("Staff agregado correctamente al equipo {}", idEquipo);
            return mapearStaffAResponse(staff);

        } catch (RecursoNoEncontradoException | ReglaNegocioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al agregar staff: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al agregar miembro del staff");
        }
    }

    @Transactional
    public RosterResponseDTO agregarJugadorAlRoster(Long idEquipo, RosterRequestDTO dto) {
        log.info("Agregando jugador {} al roster del equipo {}", dto.getIdJugador(), idEquipo);
        try {
            Equipo equipo = obtenerEquipoOFallar(idEquipo);

            JugadorRemotoDTO jugadorRemoto = jugadorClient.obtenerJugadorPorId(dto.getIdJugador());

            if (Boolean.FALSE.equals(jugadorRemoto.getActivo())) {
                throw new ReglaNegocioException(
                        "No se puede agregar al roster: el jugador " + jugadorRemoto.getNickname()
                                + " está retirado/desactivado");
            }

            if (dto.getFechaFin() != null && dto.getFechaFin().isBefore(dto.getFechaInicio())) {
                throw new ReglaNegocioException(
                        "La fecha de fin no puede ser anterior a la fecha de inicio");
            }

            RosterHistorico roster = RosterHistorico.builder()
                    .idJugador(dto.getIdJugador())
                    .nicknameAlUnirse(jugadorRemoto.getNickname())
                    .fechaInicio(dto.getFechaInicio())
                    .fechaFin(dto.getFechaFin())
                    .build();

            equipo.agregarRoster(roster);
            equipoRepository.save(equipo);

            log.info("Jugador {} agregado al roster del equipo {} - registro ID: {}",
                    jugadorRemoto.getNickname(), idEquipo, roster.getId());

            return mapearRosterAResponse(roster);

        } catch (RecursoNoEncontradoException | ReglaNegocioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al agregar al roster: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al agregar al roster");
        }
    }

    private Equipo obtenerEquipoOFallar(Long id) {
        return equipoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Equipo no encontrado con ID: {}", id);
                    return new RecursoNoEncontradoException("Equipo no encontrado con ID: " + id);
                });
    }

    private EquipoResponseDTO mapearEquipoAResponse(Equipo e) {

        int actuales = (int) e.getRoster().stream()
                .filter(r -> r.getFechaFin() == null)
                .count();

        return EquipoResponseDTO.builder()
                .id(e.getId())
                .nombre(e.getNombre())
                .region(e.getRegion())
                .fechaFundacion(e.getFechaFundacion())
                .rankingMundial(e.getRankingMundial())
                .presupuestoAnualUsd(e.getPresupuestoAnualUsd())
                .activo(e.getActivo())
                .cantidadStaff(e.getStaff().size())
                .cantidadJugadoresActuales(actuales)
                .staff(e.getStaff().stream().map(this::mapearStaffAResponse).toList())
                .roster(e.getRoster().stream().map(this::mapearRosterAResponse).toList())
                .build();
    }

    private StaffResponseDTO mapearStaffAResponse(StaffTecnico s) {
        return StaffResponseDTO.builder()
                .id(s.getId())
                .nombre(s.getNombre())
                .rolStaff(s.getRolStaff())
                .salarioMensualUsd(s.getSalarioMensualUsd())
                .activo(s.getActivo())
                .build();
    }

    private RosterResponseDTO mapearRosterAResponse(RosterHistorico r) {
        return RosterResponseDTO.builder()
                .id(r.getId())
                .idJugador(r.getIdJugador())
                .nicknameAlUnirse(r.getNicknameAlUnirse())
                .fechaInicio(r.getFechaInicio())
                .fechaFin(r.getFechaFin())
                .activo(r.getFechaFin() == null)
                .build();
    }
}
