package com.dev.Nominal.models.entity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "orden_trabajo")
public class OrdenTrabajo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//serializar el codigo 0000000
    private String codigo;
    private LocalDate fechaCreacion;
    private String estado;
//crear un costoEstimado

    // Almacena los detalles en una tabla externa
    @ElementCollection
    @CollectionTable(name = "orden_detalles", joinColumns = @JoinColumn(name = "orden_trabajo_id"))
    @Column(name = "detalle")
    private List<String> detallesReparacion;

    // Almacena las descripciones en una tabla externa
    @ElementCollection
    @CollectionTable(name = "orden_descripcion", joinColumns = @JoinColumn(name = "orden_trabajo_id"))
    @Column(name = "descripcion")
    private List<String> descripcion;

    @ElementCollection
    @CollectionTable(name = "orden_costo_Estimado", joinColumns = @JoinColumn(name = "orden_trabajo_id"))
    @Column(name = "costoEstimado")
    private List<Double> costoEstimado;

    private String observacion;
    private String metodoPago;

    @Lob
    private Double costoTotal;

    @ManyToOne
    @JoinColumn(name = "idVehiculo")
    private Vehiculo vehiculo;

    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Client cliente;

    @ManyToOne
    @JoinColumn(name = "idPersonal")
    private Personal personal;


    @OneToMany(mappedBy = "ordenTrabajo", cascade = CascadeType.ALL)
    private List<FichaTrabajo> fichaTrabajos;

    // Métodos para manipular los detalles de reparación y costos en un solo campo
    public void addDetalle(String descripcion, String detalleReparacion, Double costoEstimado) {
        this.descripcion.add(descripcion);
        this.detallesReparacion.add(detalleReparacion);
        this.costoEstimado.add(costoEstimado);
    }

    public List<String> getDetalles() {
        List<String> detalles = new ArrayList<>();
        for (int i = 0; i < descripcion.size(); i++) {
            detalles.add("Descripción: " + descripcion.get(i) + ", Detalle: " + detallesReparacion.get(i) + ", Costo Estimado: " + costoEstimado.get(i));
        }
        return detalles;
    }

    // incluir una observacion para pagos pedientes y metodo de pago;
}
