package com.esports.mstorneos.service;

import com.esports.mstorneos.client.JuegoClient;
import com.esports.mstorneos.client.JuegoRemotoDTO;
import com.esports.mstorneos.dto.*;
import com.esports.mstorneos.model.FaseTorneo;
import com.esports.mstorneos.model.Torneo;
import com.esports.mstorneos.exception.RecursoNoEncontradoException;
import com.esports.mstorneos.exception.ReglaNegocioException;
import com.esports.mstorneos.repository.FaseTorneoRepository;
import com.esports.mstorneos.repository.TorneoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TorneoService {

    private static final Logger log = LoggerFactory.getLogger(TorneoService.class);

    private final TorneoRepository torneoRepository;
    private final FaseTorneoRepository faseRepository;
    private final JuegoClient juegoClient;

    @Transactional
    public TorneoResponseDTO crearTorneo(TorneoRequestDTO dto) {
        log.info("Creando torneo: {}", dto.getNombre());
        try {

            if (torneoRepository.existsByNombre(dto.getNombre())) {
                throw new ReglaNegocioException("Ya existe un torneo con el nombre: " + dto.getNombre());
            }

            if (dto.getFechaFin().isBefore(dto.getFechaInicio())) {
                throw new ReglaNegocioException("La fecha fin debe ser posterior a la fecha inicio");
            }

            JuegoRemotoDTO juego = juegoClient.obtenerJuegoPorId(dto.getIdJuego());
            if (Boolean.FALSE.equals(juego.getActivo())) {
                throw new ReglaNegocioException(
                        "No se puede crear torneo para un juego desactivado: " + juego.getNombre());
            }

            Torneo torneo = Torneo.builder()
                    .nombre(dto.getNombre())
                    .idJuego(dto.getIdJuego())
                    .nombreJuego(juego.getNombre()) 
                    .organizador(dto.getOrganizador())
                    .fechaInicio(dto.getFechaInicio())
                    .fechaFin(dto.getFechaFin())
                    .premioTotalUsd(dto.getPremioTotalUsd())
                    .maxEquipos(dto.getMaxEquipos())
                    .modalidad(dto.getModalidad())
                    .estado("PLANIFICADO") 
                    .build();

            Torneo guardado = torneoRepository.save(torneo);
            log.info("Torneo creado - ID: {}", guardado.getId());
            return mapearAResponse(guardado);

        } catch (ReglaNegocioException | RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al crear torneo: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al crear torneo");
        }
    }

    @Transactional(readOnly = true)
    public List<TorneoResponseDTO> listar() {
        log.info("Listando torneos");
        return torneoRepository.findAll().stream().map(this::mapearAResponse).toList();
    }

    @Transactional(readOnly = true)
    public TorneoResponseDTO buscarPorId(Long id) {
        return mapearAResponse(obtenerOFallar(id));
    }

    @Transactional(readOnly = true)
    public List<TorneoResponseDTO> buscarPorEstado(String estado) {
        return torneoRepository.findByEstado(estado).stream().map(this::mapearAResponse).toList();
    }

    @Transactional
    public TorneoResponseDTO cambiarEstado(Long id, String nuevoEstado) {
        log.info("Cambiando estado del torneo {} a {}", id, nuevoEstado);
        Torneo torneo = obtenerOFallar(id);

        if ("FINALIZADO".equals(torneo.getEstado())) {
            throw new ReglaNegocioException("No se puede modificar un torneo finalizado");
        }

        if (!List.of("PLANIFICADO", "EN_CURSO", "FINALIZADO", "CANCELADO").contains(nuevoEstado)) {
            throw new ReglaNegocioException("Estado inválido: " + nuevoEstado);
        }

        torneo.setEstado(nuevoEstado);
        return mapearAResponse(torneoRepository.save(torneo));
    }

    @Transactional
    public void eliminar(Long id) {
        if (!torneoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Torneo no encontrado: " + id);
        }
        torneoRepository.deleteById(id);
        log.warn("Torneo {} eliminado", id);
    }

    @Transactional
    public FaseResponseDTO agregarFase(Long idTorneo, FaseRequestDTO dto) {
        log.info("Agregando fase '{}' al torneo {}", dto.getNombreFase(), idTorneo);
        Torneo torneo = obtenerOFallar(idTorneo);

        if (faseRepository.existsByTorneoIdAndOrden(idTorneo, dto.getOrden())) {
            throw new ReglaNegocioException(
                    "Ya existe una fase con orden " + dto.getOrden() + " en este torneo");
        }

        FaseTorneo fase = FaseTorneo.builder()
                .nombreFase(dto.getNombreFase())
                .orden(dto.getOrden())
                .fechaInicio(dto.getFechaInicio())
                .fechaFin(dto.getFechaFin())
                .formato(dto.getFormato())
                .build();

        torneo.agregarFase(fase);
        torneoRepository.save(torneo);
        return mapearFase(fase);
    }

    private Torneo obtenerOFallar(Long id) {
        return torneoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Torneo no encontrado: " + id));
    }

    private TorneoResponseDTO mapearAResponse(Torneo t) {
        return TorneoResponseDTO.builder()
                .id(t.getId())
                .nombre(t.getNombre())
                .idJuego(t.getIdJuego())
                .nombreJuego(t.getNombreJuego())
                .organizador(t.getOrganizador())
                .fechaInicio(t.getFechaInicio())
                .fechaFin(t.getFechaFin())
                .duracionDias((int) ChronoUnit.DAYS.between(t.getFechaInicio(), t.getFechaFin()))
                .premioTotalUsd(t.getPremioTotalUsd())
                .maxEquipos(t.getMaxEquipos())
                .modalidad(t.getModalidad())
                .estado(t.getEstado())
                .fases(t.getFases().stream().map(this::mapearFase).toList())
                .build();
    }

    private FaseResponseDTO mapearFase(FaseTorneo f) {
        return FaseResponseDTO.builder()
                .id(f.getId())
                .nombreFase(f.getNombreFase())
                .orden(f.getOrden())
                .fechaInicio(f.getFechaInicio())
                .fechaFin(f.getFechaFin())
                .formato(f.getFormato())
                .build();
    }
}
