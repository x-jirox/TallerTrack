package com.dev.Nominal.models.repository;

import com.dev.Nominal.models.entity.FichaTrabajo;
import com.dev.Nominal.models.entity.OrdenTrabajo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FichaTrabajoDaoImpl implements IFichaTrabajoDao{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<FichaTrabajo> findAll() {
        return em.createQuery("from FichaTrabajo", FichaTrabajo.class).getResultList();
    }

    @Override
    public void save(FichaTrabajo fichaTrabajo) {
        if (fichaTrabajo.getId() != null && fichaTrabajo.getId() > 0) {
            em.merge(fichaTrabajo);
        }else
            em.persist(fichaTrabajo);
    }

    @Override
    public FichaTrabajo findOne(Long id) {
        return em.find(FichaTrabajo.class,id);
    }

    @Override
    public void delete(Long id) {
       em.remove(findOne(id));
    }

    @Override
    public List<FichaTrabajo> findByPlaca(String term) {
        String query = "SELECT f FROM FichaTrabajo f " +
                "WHERE LOWER(f.ordenTrabajo.vehiculo.placa) LIKE LOWER(CONCAT('%', :term, '%'))";
        TypedQuery<FichaTrabajo> typedQuery = em.createQuery(query, FichaTrabajo.class);
        typedQuery.setParameter("term", term);
        return typedQuery.getResultList();
    }
}
