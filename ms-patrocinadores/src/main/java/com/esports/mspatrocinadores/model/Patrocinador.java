package com.esports.mspatrocinadores.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "patrocinadores")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patrocinador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombre_empresa", nullable = false, unique = true, length = 100)
    private String nombreEmpresa;

    @Column(name = "industria", nullable = false, length = 50)
    private String industria; 

    @Column(name = "pais_origen", nullable = false, length = 50)
    private String paisOrigen;

    @Column(name = "sitio_web", length = 200)
    private String sitioWeb;

    @Column(name = "tier", nullable = false, length = 20)
    private String tier; 

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "patrocinador", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    @Builder.Default
    private List<Contrato> contratos = new ArrayList<>();

    public void agregarContrato(Contrato c) {
        contratos.add(c);
        c.setPatrocinador(this);
    }
}
