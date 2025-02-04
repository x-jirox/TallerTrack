package com.dev.Nominal.models.service;

import com.dev.Nominal.models.entity.OrdenTrabajo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IOrdenTrabajoService {
    public List<OrdenTrabajo> findAll();
    public void save(OrdenTrabajo ordenTrabajo);
    public OrdenTrabajo findOne(Long id);
    @Query("SELECT o FROM OrdenTrabajo o " +
            "JOIN o.cliente c " +
            "JOIN o.vehiculo v " +
            "WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :term, '%')) " +
            "OR LOWER(v.placa) LIKE LOWER(CONCAT('%', :term, '%'))")
    List<OrdenTrabajo> findByClienteNombreOrVehiculoPlacaContainingIgnoreCase(@Param("term") String term);
    List<OrdenTrabajo> findByEstado(String estado);

    List<OrdenTrabajo> buscarPorFiltros(String term , LocalDate startDate , LocalDate endDate);

}
