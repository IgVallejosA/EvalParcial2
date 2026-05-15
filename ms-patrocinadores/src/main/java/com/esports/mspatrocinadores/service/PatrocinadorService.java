package com.esports.mspatrocinadores.service;

import com.esports.mspatrocinadores.client.EquipoClient;
import com.esports.mspatrocinadores.client.EquipoRemotoDTO;
import com.esports.mspatrocinadores.dto.*;
import com.esports.mspatrocinadores.model.Contrato;
import com.esports.mspatrocinadores.model.Patrocinador;
import com.esports.mspatrocinadores.exception.RecursoNoEncontradoException;
import com.esports.mspatrocinadores.exception.ReglaNegocioException;
import com.esports.mspatrocinadores.repository.PatrocinadorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PatrocinadorService {

    private static final Logger log = LoggerFactory.getLogger(PatrocinadorService.class);

    private final PatrocinadorRepository patrocinadorRepository;
    private final EquipoClient equipoClient;

    @Transactional
    public PatrocinadorResponseDTO crear(PatrocinadorRequestDTO dto) {
        log.info("Creando patrocinador: {}", dto.getNombreEmpresa());
        try {
            if (patrocinadorRepository.existsByNombreEmpresa(dto.getNombreEmpresa())) {
                throw new ReglaNegocioException(
                        "Ya existe un patrocinador con el nombre: " + dto.getNombreEmpresa());
            }

            Patrocinador p = Patrocinador.builder()
                    .nombreEmpresa(dto.getNombreEmpresa())
                    .industria(dto.getIndustria())
                    .paisOrigen(dto.getPaisOrigen())
                    .sitioWeb(dto.getSitioWeb())
                    .tier(dto.getTier())
                    .activo(true)
                    .build();

            Patrocinador guardado = patrocinadorRepository.save(p);
            log.info("Patrocinador creado - ID: {}", guardado.getId());
            return mapearPatrocinadorAResponse(guardado);

        } catch (ReglaNegocioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al crear patrocinador: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al crear patrocinador");
        }
    }

    @Transactional(readOnly = true)
    public List<PatrocinadorResponseDTO> listar() {
        return patrocinadorRepository.findAll().stream().map(this::mapearPatrocinadorAResponse).toList();
    }

    @Transactional(readOnly = true)
    public PatrocinadorResponseDTO buscarPorId(Long id) {
        return mapearPatrocinadorAResponse(obtenerOFallar(id));
    }

    @Transactional(readOnly = true)
    public List<PatrocinadorResponseDTO> buscarPorTier(String tier) {
        return patrocinadorRepository.findByTier(tier).stream().map(this::mapearPatrocinadorAResponse).toList();
    }

    @Transactional
    public PatrocinadorResponseDTO actualizar(Long id, PatrocinadorRequestDTO dto) {
        Patrocinador p = obtenerOFallar(id);

        if (!p.getNombreEmpresa().equals(dto.getNombreEmpresa())
                && patrocinadorRepository.existsByNombreEmpresa(dto.getNombreEmpresa())) {
            throw new ReglaNegocioException("Ya existe otro patrocinador con ese nombre");
        }

        p.setNombreEmpresa(dto.getNombreEmpresa());
        p.setIndustria(dto.getIndustria());
        p.setPaisOrigen(dto.getPaisOrigen());
        p.setSitioWeb(dto.getSitioWeb());
        p.setTier(dto.getTier());
        return mapearPatrocinadorAResponse(patrocinadorRepository.save(p));
    }

    @Transactional
    public void desactivar(Long id) {
        Patrocinador p = obtenerOFallar(id);
        if (!p.getActivo())
            throw new ReglaNegocioException("Patrocinador ya desactivado");

        long activos = p.getContratos().stream().filter(Contrato::getActivo).count();
        if (activos > 0) {
            throw new ReglaNegocioException(
                    "No se puede desactivar: tiene " + activos + " contratos activos");
        }

        p.setActivo(false);
        patrocinadorRepository.save(p);
        log.info("Patrocinador {} desactivado", id);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!patrocinadorRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Patrocinador no encontrado: " + id);
        }
        patrocinadorRepository.deleteById(id);
    }

    @Transactional
    public ContratoResponseDTO agregarContrato(Long idPatrocinador, ContratoRequestDTO dto) {
        log.info("Agregando contrato del patrocinador {} con equipo {}", idPatrocinador, dto.getIdEquipo());
        try {
            Patrocinador patrocinador = obtenerOFallar(idPatrocinador);

            if (dto.getFechaFin().isBefore(dto.getFechaInicio())) {
                throw new ReglaNegocioException("La fecha fin debe ser posterior a la fecha inicio");
            }

            EquipoRemotoDTO equipo = equipoClient.obtenerEquipoPorId(dto.getIdEquipo());
            if (Boolean.FALSE.equals(equipo.getActivo())) {
                throw new ReglaNegocioException("No se puede contratar con equipo inactivo: " + equipo.getNombre());
            }

            Contrato c = Contrato.builder()
                    .idEquipo(dto.getIdEquipo())
                    .nombreEquipo(equipo.getNombre())
                    .montoAnualUsd(dto.getMontoAnualUsd())
                    .fechaInicio(dto.getFechaInicio())
                    .fechaFin(dto.getFechaFin())
                    .tipoAcuerdo(dto.getTipoAcuerdo())
                    .activo(true)
                    .build();

            patrocinador.agregarContrato(c);
            patrocinadorRepository.save(patrocinador);

            log.info("Contrato creado entre patrocinador {} y equipo {}", idPatrocinador, equipo.getNombre());
            return mapearContratoAResponse(c);

        } catch (ReglaNegocioException | RecursoNoEncontradoException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al crear contrato: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al crear contrato");
        }
    }

    private Patrocinador obtenerOFallar(Long id) {
        return patrocinadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Patrocinador no encontrado: " + id));
    }

    private PatrocinadorResponseDTO mapearPatrocinadorAResponse(Patrocinador p) {
        return PatrocinadorResponseDTO.builder()
                .id(p.getId())
                .nombreEmpresa(p.getNombreEmpresa())
                .industria(p.getIndustria())
                .paisOrigen(p.getPaisOrigen())
                .sitioWeb(p.getSitioWeb())
                .tier(p.getTier())
                .activo(p.getActivo())
                .cantidadContratos(p.getContratos().size())
                .contratos(p.getContratos().stream().map(this::mapearContratoAResponse).toList())
                .build();
    }

    private ContratoResponseDTO mapearContratoAResponse(Contrato c) {
        return ContratoResponseDTO.builder()
                .id(c.getId())
                .idEquipo(c.getIdEquipo())
                .nombreEquipo(c.getNombreEquipo())
                .montoAnualUsd(c.getMontoAnualUsd())
                .fechaInicio(c.getFechaInicio())
                .fechaFin(c.getFechaFin())
                .tipoAcuerdo(c.getTipoAcuerdo())
                .activo(c.getActivo())
                .build();
    }
}
