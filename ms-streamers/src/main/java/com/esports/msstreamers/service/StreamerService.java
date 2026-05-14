package com.esports.msstreamers.service;

import com.esports.msstreamers.client.TorneoClient;
import com.esports.msstreamers.client.TorneoRemotoDTO;
import com.esports.msstreamers.dto.*;
import com.esports.msstreamers.model.AsignacionCobertura;
import com.esports.msstreamers.model.Streamer;
import com.esports.msstreamers.exception.RecursoNoEncontradoException;
import com.esports.msstreamers.exception.ReglaNegocioException;
import com.esports.msstreamers.repository.AsignacionCoberturaRepository;
import com.esports.msstreamers.repository.StreamerRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StreamerService {

    private static final Logger log = LoggerFactory.getLogger(StreamerService.class);

    private final StreamerRepository streamerRepository;
    private final AsignacionCoberturaRepository asignacionRepository;
    private final TorneoClient torneoClient;

    @Transactional
    public StreamerResponseDTO crear(StreamerRequestDTO dto) {
        log.info("Creando streamer: {}", dto.getNombreArtistico());
        try {
            if (streamerRepository.existsByNombreArtistico(dto.getNombreArtistico())) {
                throw new ReglaNegocioException(
                        "Ya existe un streamer con el nombre: " + dto.getNombreArtistico());
            }

            Streamer s = Streamer.builder()
                    .nombreArtistico(dto.getNombreArtistico())
                    .nombreReal(dto.getNombreReal())
                    .pais(dto.getPais())
                    .idioma(dto.getIdioma())
                    .rol(dto.getRol())
                    .plataformaPrincipal(dto.getPlataformaPrincipal())
                    .seguidores(dto.getSeguidores() != null ? dto.getSeguidores() : 0)
                    .activo(true)
                    .build();

            Streamer guardado = streamerRepository.save(s);
            log.info("Streamer creado - ID: {}", guardado.getId());
            return mapearStreamerAResponse(guardado);

        } catch (ReglaNegocioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al crear streamer: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al crear streamer");
        }
    }

    @Transactional(readOnly = true)
    public List<StreamerResponseDTO> listar() {
        return streamerRepository.findAll().stream().map(this::mapearStreamerAResponse).toList();
    }

    @Transactional(readOnly = true)
    public StreamerResponseDTO buscarPorId(Long id) {
        return mapearStreamerAResponse(obtenerOFallar(id));
    }

    @Transactional(readOnly = true)
    public List<StreamerResponseDTO> buscarPorIdioma(String idioma) {
        return streamerRepository.findByIdioma(idioma).stream().map(this::mapearStreamerAResponse).toList();
    }

    @Transactional
    public StreamerResponseDTO actualizar(Long id, StreamerRequestDTO dto) {
        Streamer s = obtenerOFallar(id);
        if (!s.getNombreArtistico().equals(dto.getNombreArtistico())
                && streamerRepository.existsByNombreArtistico(dto.getNombreArtistico())) {
            throw new ReglaNegocioException("Nombre artístico ya en uso");
        }
        s.setNombreArtistico(dto.getNombreArtistico());
        s.setNombreReal(dto.getNombreReal());
        s.setPais(dto.getPais());
        s.setIdioma(dto.getIdioma());
        s.setRol(dto.getRol());
        s.setPlataformaPrincipal(dto.getPlataformaPrincipal());
        s.setSeguidores(dto.getSeguidores());
        return mapearStreamerAResponse(streamerRepository.save(s));
    }

    @Transactional
    public void desactivar(Long id) {
        Streamer s = obtenerOFallar(id);
        if (!s.getActivo())
            throw new ReglaNegocioException("Streamer ya desactivado");
        s.setActivo(false);
        streamerRepository.save(s);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!streamerRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Streamer no encontrado: " + id);
        }
        streamerRepository.deleteById(id);
    }

    @Transactional
    public AsignacionResponseDTO asignarCobertura(Long idStreamer, AsignacionRequestDTO dto) {
        log.info("Asignando streamer {} al torneo {}", idStreamer, dto.getIdTorneo());
        try {
            Streamer s = obtenerOFallar(idStreamer);

            if (asignacionRepository.existsByStreamerIdAndIdTorneo(idStreamer, dto.getIdTorneo())) {
                throw new ReglaNegocioException("El streamer ya tiene asignada cobertura de ese torneo");
            }

            if (dto.getFechaFinCobertura().isBefore(dto.getFechaInicioCobertura())) {
                throw new ReglaNegocioException("Fecha fin de cobertura debe ser posterior a fecha inicio");
            }

            TorneoRemotoDTO torneo = torneoClient.obtenerTorneoPorId(dto.getIdTorneo());
            if ("CANCELADO".equals(torneo.getEstado()) || "FINALIZADO".equals(torneo.getEstado())) {
                throw new ReglaNegocioException(
                        "No se puede asignar cobertura a torneo en estado: " + torneo.getEstado());
            }

            AsignacionCobertura a = AsignacionCobertura.builder()
                    .idTorneo(dto.getIdTorneo())
                    .nombreTorneo(torneo.getNombre())
                    .fechaInicioCobertura(dto.getFechaInicioCobertura())
                    .fechaFinCobertura(dto.getFechaFinCobertura())
                    .honorariosUsd(dto.getHonorariosUsd())
                    .build();

            s.agregarCobertura(a);
            streamerRepository.save(s);

            log.info("Cobertura asignada: streamer {} -> torneo {}", idStreamer, torneo.getNombre());
            return mapearAsignacionAResponse(a);

        } catch (ReglaNegocioException | RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al asignar cobertura: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al asignar cobertura");
        }
    }

    private Streamer obtenerOFallar(Long id) {
        return streamerRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Streamer no encontrado: " + id));
    }

    private StreamerResponseDTO mapearStreamerAResponse(Streamer s) {
        return StreamerResponseDTO.builder()
                .id(s.getId())
                .nombreArtistico(s.getNombreArtistico())
                .nombreReal(s.getNombreReal())
                .pais(s.getPais())
                .idioma(s.getIdioma())
                .rol(s.getRol())
                .plataformaPrincipal(s.getPlataformaPrincipal())
                .seguidores(s.getSeguidores())
                .activo(s.getActivo())
                .cantidadCoberturas(s.getCoberturas().size())
                .coberturas(s.getCoberturas().stream().map(this::mapearAsignacionAResponse).toList())
                .build();
    }

    private AsignacionResponseDTO mapearAsignacionAResponse(AsignacionCobertura a) {
        return AsignacionResponseDTO.builder()
                .id(a.getId())
                .idTorneo(a.getIdTorneo())
                .nombreTorneo(a.getNombreTorneo())
                .fechaInicioCobertura(a.getFechaInicioCobertura())
                .fechaFinCobertura(a.getFechaFinCobertura())
                .honorariosUsd(a.getHonorariosUsd())
                .build();
    }
}
