package com.esports.msjuegos.service;

import com.esports.msjuegos.dto.*;
import com.esports.msjuegos.model.Juego;
import com.esports.msjuegos.model.ModoCompetitivo;
import com.esports.msjuegos.exception.RecursoNoEncontradoException;
import com.esports.msjuegos.exception.ReglaNegocioException;
import com.esports.msjuegos.repository.JuegoRepository;
import com.esports.msjuegos.repository.ModoCompetitivoRepository;
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
public class JuegoService {

    private static final Logger log = LoggerFactory.getLogger(JuegoService.class);

    private final JuegoRepository juegoRepository;
    private final ModoCompetitivoRepository modoRepository;

    @Transactional
    public JuegoResponseDTO crearJuego(JuegoRequestDTO dto) {
        log.info("Creando juego: {}", dto.getNombre());

        try {
            if (juegoRepository.existsByNombre(dto.getNombre())) {
                log.warn("Juego duplicado rechazado: {}", dto.getNombre());
                throw new ReglaNegocioException(
                        "Ya existe un juego registrado con el nombre: " + dto.getNombre());
            }

            Juego juego = Juego.builder()
                    .nombre(dto.getNombre())
                    .genero(dto.getGenero())
                    .desarrolladora(dto.getDesarrolladora())
                    .fechaLanzamiento(dto.getFechaLanzamiento())
                    .plataforma(dto.getPlataforma())
                    .prizePoolTotalUsd(dto.getPrizePoolTotalUsd() != null
                            ? dto.getPrizePoolTotalUsd()
                            : 0.0)
                    .activo(true)
                    .build();

            Juego guardado = juegoRepository.save(juego);
            log.info("Juego creado - ID: {}, Nombre: {}", guardado.getId(), guardado.getNombre());

            return mapearJuegoAResponse(guardado);

        } catch (ReglaNegocioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al crear juego: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al crear el juego: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public List<JuegoResponseDTO> listarJuegos() {
        log.info("Listando todos los juegos del catálogo");
        return juegoRepository.findAll().stream()
                .map(this::mapearJuegoAResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public JuegoResponseDTO buscarJuegoPorId(Long id) {
        log.info("Buscando juego con ID: {}", id);
        Juego juego = obtenerJuegoOFallar(id);
        return mapearJuegoAResponse(juego);
    }

    @Transactional(readOnly = true)
    public List<JuegoResponseDTO> buscarPorGenero(String genero) {
        log.info("Buscando juegos del género: {}", genero);
        List<JuegoResponseDTO> resultado = juegoRepository.findByGenero(genero).stream()
                .map(this::mapearJuegoAResponse)
                .toList();
        log.info("Juegos encontrados con género {}: {}", genero, resultado.size());
        return resultado;
    }

    @Transactional
    public JuegoResponseDTO actualizarJuego(Long id, JuegoRequestDTO dto) {
        log.info("Actualizando juego ID: {}", id);
        try {
            Juego juego = obtenerJuegoOFallar(id);

            if (!juego.getNombre().equals(dto.getNombre())
                    && juegoRepository.existsByNombre(dto.getNombre())) {
                throw new ReglaNegocioException(
                        "Ya existe otro juego con el nombre: " + dto.getNombre());
            }

            juego.setNombre(dto.getNombre());
            juego.setGenero(dto.getGenero());
            juego.setDesarrolladora(dto.getDesarrolladora());
            juego.setFechaLanzamiento(dto.getFechaLanzamiento());
            juego.setPlataforma(dto.getPlataforma());
            juego.setPrizePoolTotalUsd(dto.getPrizePoolTotalUsd());

            Juego actualizado = juegoRepository.save(juego);
            log.info("Juego actualizado correctamente: {}", actualizado.getId());
            return mapearJuegoAResponse(actualizado);

        } catch (RecursoNoEncontradoException | ReglaNegocioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar juego {}: {}", id, e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al actualizar el juego");
        }
    }

    @Transactional
    public void desactivarJuego(Long id) {
        log.info("Desactivando juego ID: {}", id);
        Juego juego = obtenerJuegoOFallar(id);

        if (!juego.getActivo()) {
            throw new ReglaNegocioException("El juego ya se encuentra desactivado");
        }

        juego.setActivo(false);
        juegoRepository.save(juego);
        log.info("Juego {} desactivado correctamente", id);
    }

    @Transactional
    public void eliminarJuego(Long id) {
        log.info("Eliminando físicamente juego ID: {}", id);
        if (!juegoRepository.existsById(id)) {
            throw new RecursoNoEncontradoException("Juego no encontrado con ID: " + id);
        }
        juegoRepository.deleteById(id);
        log.warn("Juego {} eliminado FÍSICAMENTE de la BD", id);
    }

    @Transactional
    public ModoCompetitivoResponseDTO agregarModo(Long idJuego, ModoCompetitivoRequestDTO dto) {
        log.info("Agregando modo '{}' al juego {}", dto.getNombreModo(), idJuego);
        try {
            Juego juego = obtenerJuegoOFallar(idJuego);

            if (modoRepository.existsByJuegoIdAndNombreModo(idJuego, dto.getNombreModo())) {
                log.warn("Modo duplicado rechazado: '{}' ya existe en juego {}",
                        dto.getNombreModo(), idJuego);
                throw new ReglaNegocioException(
                        "El juego ya tiene un modo con el nombre: " + dto.getNombreModo());
            }

            ModoCompetitivo modo = ModoCompetitivo.builder()
                    .nombreModo(dto.getNombreModo())
                    .maxJugadoresPorEquipo(dto.getMaxJugadoresPorEquipo())
                    .duracionPromedioMinutos(dto.getDuracionPromedioMinutos())
                    .activo(true)
                    .build();

            juego.agregarModo(modo);
            juegoRepository.save(juego);

            log.info("Modo '{}' agregado al juego {}", modo.getNombreModo(), idJuego);
            return mapearModoAResponse(modo);

        } catch (RecursoNoEncontradoException | ReglaNegocioException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al agregar modo: {}", e.getMessage(), e);
            throw new ReglaNegocioException("Error interno al agregar modo competitivo");
        }
    }

    @Transactional(readOnly = true)
    public List<ModoCompetitivoResponseDTO> listarModosDeJuego(Long idJuego) {
        log.info("Listando modos del juego ID: {}", idJuego);
        if (!juegoRepository.existsById(idJuego)) {
            throw new RecursoNoEncontradoException("Juego no encontrado con ID: " + idJuego);
        }
        return modoRepository.findByJuegoId(idJuego).stream()
                .map(this::mapearModoAResponse)
                .toList();
    }

    private Juego obtenerJuegoOFallar(Long id) {
        return juegoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Juego no encontrado con ID: {}", id);
                    return new RecursoNoEncontradoException("Juego no encontrado con ID: " + id);
                });
    }

    private int calcularAniosEnMercado(LocalDate fechaLanzamiento) {
        return Period.between(fechaLanzamiento, LocalDate.now()).getYears();
    }

    private JuegoResponseDTO mapearJuegoAResponse(Juego j) {
        return JuegoResponseDTO.builder()
                .id(j.getId())
                .nombre(j.getNombre())
                .genero(j.getGenero())
                .desarrolladora(j.getDesarrolladora())
                .fechaLanzamiento(j.getFechaLanzamiento())
                .aniosEnMercado(calcularAniosEnMercado(j.getFechaLanzamiento()))
                .plataforma(j.getPlataforma())
                .prizePoolTotalUsd(j.getPrizePoolTotalUsd())
                .activo(j.getActivo())
                .cantidadModos(j.getModos().size())
                .modos(j.getModos().stream().map(this::mapearModoAResponse).toList())
                .build();
    }

    private ModoCompetitivoResponseDTO mapearModoAResponse(ModoCompetitivo m) {
        return ModoCompetitivoResponseDTO.builder()
                .id(m.getId())
                .nombreModo(m.getNombreModo())
                .maxJugadoresPorEquipo(m.getMaxJugadoresPorEquipo())
                .duracionPromedioMinutos(m.getDuracionPromedioMinutos())
                .activo(m.getActivo())
                .build();
    }
}
