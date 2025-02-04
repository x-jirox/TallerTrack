package com.dev.Nominal.models.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "empleado")
@Data
public class Personal implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String cedula;

    @OneToMany(mappedBy = "personal", cascade = CascadeType.ALL)
    private List<OrdenTrabajo> ordenesTrabajo;
}
