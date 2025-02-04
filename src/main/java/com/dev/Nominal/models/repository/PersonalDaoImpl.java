package com.dev.Nominal.models.repository;

import com.dev.Nominal.models.entity.Personal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PersonalDaoImpl implements IPersonalDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Personal> findAll() {
        return em.createQuery("from Personal", Personal.class).getResultList();
    }

    @Override
    public void save(Personal personal) {
        if (personal.getId() != null  &&  personal.getId()>0) {
            em.merge(personal);
        }else{
            em.persist(personal);
        }
    }

    @Override
    public Personal findOne(Long id) {
        return em.find(Personal.class, id);
    }

    @Override
    public void delete(Long id) {
        em.remove(findOne(id));
    }
}
