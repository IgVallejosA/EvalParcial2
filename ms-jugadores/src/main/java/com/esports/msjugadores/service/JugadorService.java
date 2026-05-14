package com.esports.msjugadores.service;

import com.esports.msjugadores.dto.JugadorRequestDTO;
import com.esports.msjugadores.dto.JugadorResponseDTO;
import com.esports.msjugadores.model.Jugador;
import com.esports.msjugadores.exception.RecursoNoEncontradoException;
import com.esports.msjugadores.exception.ReglaNegocioException;
import com.esports.msjugadores.repository.JugadorRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class JugadorService {

    private static final Logger log = LoggerFactory.getLogger(JugadorService.class);

    private final JugadorRepository jugadorRepository;

    private static final int EDAD_MINIMA_PROFESIONAL = 16;

    @Transactional
    public JugadorResponseDTO crear(JugadorRequestDTO dto) {
        log.info("Iniciando creación de jugador con nickname: {}", dto.getNickname());

        try {
            if (jugadorRepository.existsByNickname(dto.getNickname())) {
                log.warn("Nickname duplicado rechazado: {}", dto.getNickname());
                throw new ReglaNegocioException(
                        "Ya existe un jugador registrado con el nickname: " + dto.getNickname());
            }
            int edad = calcularEdad(dto.getFechaNacimiento());
            if (edad < EDAD_MINIMA_PROFESIONAL) {
                log.warn("Jugador rechazado por edad insuficiente: {} años (mínimo {})",
                        edad, EDAD_MINIMA_PROFESIONAL);
                throw new ReglaNegocioException(
                        "El jugador debe tener al menos " + EDAD_MINIMA_PROFESIONAL
                                + " años para competir. Edad actual: " + edad);
            }

            Jugador jugador = Jugador.builder()
                    .nickname(dto.getNickname())
                    .nombreReal(dto.getNombreReal())
                    .pais(dto.getPais())
                    .fechaNacimiento(dto.getFechaNacimiento())
                    .rol(dto.getRol())
                    .idEquipoActual(dto.getIdEquipoActual())
                    .salarioMensualUsd(dto.getSalarioMensualUsd())
                    .activo(true)
                    .build();

            Jugador guardado = jugadorRepository.save(jugador);
            log.info("Jugador creado exitosamente - ID: {}, Nickname: {}", guardado.getId(), guardado.getNickname());

            return mapearAResponse(guardado);

        } catch (ReglaNegocioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al crear jugador '{}': {}", dto.getNickname(), e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al crear el jugador: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<JugadorResponseDTO> listarTodos() {
        log.info("Consultando todos los jugadores");
        List<JugadorResponseDTO> lista = jugadorRepository.findAll().stream()
                .map(this::mapearAResponse)
                .toList();
        log.info("Total jugadores encontrados: {}", lista.size());
        return lista;
    }

    @Transactional(readOnly = true)
    public JugadorResponseDTO buscarPorId(Long id) {
        log.info("Buscando jugador con ID: {}", id);
        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Jugador no encontrado con ID: {}", id);
                    return new RecursoNoEncontradoException("Jugador no encontrado con ID: " + id);
                });
        return mapearAResponse(jugador);
    }

    @Transactional(readOnly = true)
    public List<JugadorResponseDTO> buscarPorPais(String pais) {
        log.info("Buscando jugadores del país: {}", pais);
        List<JugadorResponseDTO> lista = jugadorRepository.findByPais(pais).stream()
                .map(this::mapearAResponse)
                .toList();
        log.info("Jugadores encontrados de {}: {}", pais, lista.size());
        return lista;
    }

    @Transactional
    public JugadorResponseDTO actualizar(Long id, JugadorRequestDTO dto) {
        log.info("Actualizando jugador con ID: {}", id);

        try {
            Jugador existente = jugadorRepository.findById(id)
                    .orElseThrow(() -> {
                        log.warn("No se puede actualizar - jugador no encontrado ID: {}", id);
                        return new RecursoNoEncontradoException(
                                "No se puede actualizar. Jugador no encontrado con ID: " + id);
                    });

            if (!existente.getNickname().equals(dto.getNickname())
                    && jugadorRepository.existsByNickname(dto.getNickname())) {
                log.warn("Actualización rechazada: nickname '{}' ya está en uso", dto.getNickname());
                throw new ReglaNegocioException(
                        "El nickname ya está en uso por otro jugador: " + dto.getNickname());
            }

            existente.setNickname(dto.getNickname());
            existente.setNombreReal(dto.getNombreReal());
            existente.setPais(dto.getPais());
            existente.setFechaNacimiento(dto.getFechaNacimiento());
            existente.setRol(dto.getRol());
            existente.setIdEquipoActual(dto.getIdEquipoActual());
            existente.setSalarioMensualUsd(dto.getSalarioMensualUsd());

            Jugador actualizado = jugadorRepository.save(existente);
            log.info("Jugador actualizado correctamente - ID: {}", actualizado.getId());

            return mapearAResponse(actualizado);

        } catch (RecursoNoEncontradoException | ReglaNegocioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar jugador {}: {}", id, e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al actualizar el jugador");
        }
    }

    @Transactional
    public void desactivar(Long id) {
        log.info("Desactivando jugador con ID: {}", id);

        Jugador jugador = jugadorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("No se puede desactivar - jugador no encontrado ID: {}", id);
                    return new RecursoNoEncontradoException(
                            "No se puede desactivar. Jugador no encontrado con ID: " + id);
                });

        if (!jugador.getActivo()) {
            log.warn("Intento de desactivar jugador ya inactivo - ID: {}", id);
            throw new ReglaNegocioException("El jugador ya se encuentra desactivado");
        }

        jugador.setActivo(false);
        jugador.setIdEquipoActual(null);
        jugadorRepository.save(jugador);
        log.info("Jugador {} desactivado correctamente", id);
    }

    @Transactional
    public void eliminar(Long id) {
        log.info("Eliminando físicamente jugador con ID: {}", id);

        if (!jugadorRepository.existsById(id)) {
            log.warn("No se puede eliminar - jugador no encontrado ID: {}", id);
            throw new RecursoNoEncontradoException("Jugador no encontrado con ID: " + id);
        }

        jugadorRepository.deleteById(id);
        log.warn("Jugador {} eliminado FÍSICAMENTE de la BD", id);
    }

    private int calcularEdad(LocalDate fechaNacimiento) {
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }

    private JugadorResponseDTO mapearAResponse(Jugador j) {
        return JugadorResponseDTO.builder()
                .id(j.getId())
                .nickname(j.getNickname())
                .nombreReal(j.getNombreReal())
                .pais(j.getPais())
                .fechaNacimiento(j.getFechaNacimiento())
                .edad(calcularEdad(j.getFechaNacimiento()))
                .rol(j.getRol())
                .idEquipoActual(j.getIdEquipoActual())
                .activo(j.getActivo())
                .salarioMensualUsd(j.getSalarioMensualUsd())
                .build();
    }
}
