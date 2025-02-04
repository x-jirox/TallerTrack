package com.dev.Nominal.models.repository;

import com.dev.Nominal.models.entity.Client;
import com.dev.Nominal.models.entity.OrdenTrabajo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public class OrdenTrabajoImplDao implements IOrdenTrabajoDao {
    @PersistenceContext
    private EntityManager em;

    @SuppressWarnings("UNCHEKED")

    @Override
    public List<OrdenTrabajo> findAll() {
        return em.createQuery("from OrdenTrabajo", OrdenTrabajo.class).getResultList();
    }

    @Override
    public void save(OrdenTrabajo ordenTrabajo) {
        if (ordenTrabajo.getId() != null && ordenTrabajo.getId() > 0) {
            em.merge(ordenTrabajo);
        }else{
            em.persist(ordenTrabajo);
        }
    }

    @Override
    public OrdenTrabajo findOne(Long id) {
        return em.find(OrdenTrabajo.class, id);
    }

    @Override
    public List<OrdenTrabajo> findByClienteNombreOrVehiculoPlacaContainingIgnoreCase(String term) {
        String query = "SELECT o FROM OrdenTrabajo o " +
                "JOIN o.cliente c " +
                "JOIN o.vehiculo v " +
                "WHERE LOWER(c.ci_ruc) LIKE LOWER(CONCAT('%', :term, '%')) " +
                "OR LOWER(v.placa) LIKE LOWER(CONCAT('%', :term, '%'))";

        TypedQuery<OrdenTrabajo> typedQuery = em.createQuery(query, OrdenTrabajo.class);
        typedQuery.setParameter("term", term.toLowerCase());

        return typedQuery.getResultList();
    }


    @Override
    public List<OrdenTrabajo> findByEstado(String estado) {
        String query = "SELECT o FROM OrdenTrabajo o WHERE o.estado = :estado";
        TypedQuery<OrdenTrabajo> typedQuery = em.createQuery(query, OrdenTrabajo.class);
        typedQuery.setParameter("estado", estado);
        return typedQuery.getResultList();
    }

    @Override
    public List<OrdenTrabajo> buscarPorFiltros(String term, LocalDate startDate, LocalDate endDate) {
        // Construcción de la consulta base
        StringBuilder query = new StringBuilder("SELECT o FROM OrdenTrabajo o WHERE 1=1");

        // Verificar si se pasó el término de búsqueda (cliente o placa)
        if (term != null && !term.isEmpty()) {
            query.append(" AND (o.cliente.ci_ruc LIKE :term OR o.vehiculo.placa LIKE :term)");
        }

        // Verificar si se pasó la fecha de inicio
        if (startDate != null) {
            query.append(" AND o.fechaCreacion >= :startDate");
        }

        // Verificar si se pasó la fecha de fin
        if (endDate != null) {
            query.append(" AND o.fechaCreacion <= :endDate");
        }

        // Crear la consulta con el entity manager
        TypedQuery<OrdenTrabajo> typedQuery = em.createQuery(query.toString(), OrdenTrabajo.class);

        // Asignar el parámetro de búsqueda por cliente o placa
        if (term != null && !term.isEmpty()) {
            typedQuery.setParameter("term", "%" + term + "%");
        }

        // Asignar el parámetro de fecha inicial
        if (startDate != null) {
            typedQuery.setParameter("startDate", startDate);
        }

        // Asignar el parámetro de fecha final
        if (endDate != null) {
            typedQuery.setParameter("endDate", endDate);
        }

        // Retornar el resultado de la consulta
        return typedQuery.getResultList();
    }



}
