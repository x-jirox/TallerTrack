package com.dev.Nominal.models.entity;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "ficha_trabajo")
public class FichaTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String descripcion;
    private LocalDate fechaCreacion;
    private Double costo;

    @ManyToOne
    @JoinColumn(name = "idOrden")
    private OrdenTrabajo ordenTrabajo;
}
