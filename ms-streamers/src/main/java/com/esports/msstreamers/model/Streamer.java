package com.esports.msstreamers.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "streamers")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class Streamer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_artistico", nullable = false, unique = true, length = 80)
    private String nombreArtistico;

    @Column(name = "nombre_real", length = 100)
    private String nombreReal;

    @Column(name = "pais", nullable = false, length = 50)
    private String pais;

    @Column(name = "idioma", nullable = false, length = 30)
    private String idioma; 

    @Column(name = "rol", nullable = false, length = 30)
    private String rol; 

    @Column(name = "plataforma_principal", nullable = false, length = 30)
    private String plataformaPrincipal; 

    @Column(name = "seguidores")
    private Integer seguidores;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "streamer",
               cascade = CascadeType.ALL,
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<AsignacionCobertura> coberturas = new ArrayList<>();

    public void agregarCobertura(AsignacionCobertura a) {
        coberturas.add(a);
        a.setStreamer(this);
    }
}
