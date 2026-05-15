package com.esports.msjuegos.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "juegos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Juego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre", nullable = false, unique = true, length = 100)
    private String nombre;

    // Género: MOBA, FPS, RTS, FIGHTING, BR, CARDS, SPORTS
    @Column(name = "genero", nullable = false, length = 20)
    private String genero;

    @Column(name = "desarrolladora", nullable = false, length = 100)
    private String desarrolladora;

    @Column(name = "fecha_lanzamiento", nullable = false)
    private LocalDate fechaLanzamiento;

    // Plataformas: "PC,PS5,XBOX"
    @Column(name = "plataforma", nullable = false, length = 100)
    private String plataforma;

    // Premio acumulado histórico de torneos en USD
    @Column(name = "prize_pool_total_usd")
    private Double prizePoolTotalUsd;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "juego", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ModoCompetitivo> modos = new ArrayList<>();

    public void agregarModo(ModoCompetitivo m) {
        modos.add(m);
        m.setJuego(this);
    }
}
