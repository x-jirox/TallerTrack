package com.dev.Nominal.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "vehiculo")
public class Vehiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private int anyo;
    private int kilometraje;
    private String placa;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    @JsonIgnore // Evita la serialización recursiva
    private Client cliente;

    @OneToMany(mappedBy = "vehiculo", cascade = CascadeType.ALL)
    @JsonIgnore // Evita la serialización recursiva
    private List<OrdenTrabajo> ordenesTrabajo;
}
