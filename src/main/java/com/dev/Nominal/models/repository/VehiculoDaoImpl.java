package com.dev.Nominal.models.repository;

import com.dev.Nominal.models.entity.Client;
import com.dev.Nominal.models.entity.Vehiculo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class VehiculoDaoImpl implements IVehiculoDao{

    @PersistenceContext
    private EntityManager em;
    @Override
    public List<Vehiculo> findAll() {
        return em.createQuery("from Vehiculo", Vehiculo.class).getResultList();
    }

    @Override
    public void save(Vehiculo vehiculo) {
        if (vehiculo.getId() != null && vehiculo.getId() > 0) {
            em.merge(vehiculo);
        }else
            em.persist(vehiculo);
    }

    @Override
    public Vehiculo findOne(Long id) {
        return em.find(Vehiculo.class, id);
    }

    @Override
    public void delete(Long id) {
        em.remove(findOne(id));
    }

    @Override
    public List<Vehiculo> findByMarcaOrModeloOrPlacaOrAnyoContainingIgnoreCase(String term) {
        String query = "SELECT v FROM Vehiculo v WHERE LOWER(v.marca) LIKE LOWER(CONCAT('%', :term, '%')) " +
                "OR LOWER(v.modelo) LIKE LOWER(CONCAT('%', :term, '%')) " +
                "OR LOWER(v.placa) LIKE LOWER(CONCAT('%', :term, '%')) " +
                "OR LOWER(CAST(v.anyo AS string)) LIKE LOWER(CONCAT('%', :term, '%'))";
        return em.createQuery(query, Vehiculo.class)
                .setParameter("term", term)
                .getResultList();
    }

}
