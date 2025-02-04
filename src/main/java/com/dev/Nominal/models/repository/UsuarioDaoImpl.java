package com.dev.Nominal.models.repository;


import com.dev.Nominal.models.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public class UsuarioDaoImpl implements IUsuarioDao {
    @PersistenceContext
    private EntityManager em;



    @SuppressWarnings("undechecked")

    @Override
    public List<User> findAll() {
        return em.createQuery("from Usuario", User.class).getResultList();
    }

    @Override
    public void save(User usuario) {
        if(usuario.getId() != null && usuario.getId()>0){
            em.merge(usuario);
        }else{
            em.persist(usuario);
        }
    }

    @Override
    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    @Override
    public void delete(Long id) {
        em.remove(findOne(id));
    }

    @Override
    public List<User> findByNombreContainingIgnoreCase(String nombres) {
        String jpql = "SELECT u FROM User u WHERE LOWER(u.nombres) LIKE LOWER(:nombres)";
        return em.createQuery(jpql, User.class)
                .setParameter("nombres", "%" + nombres + "%")
                .getResultList();
    }

    @Override
    public User findByUsername(String username) {
        try {
            return em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                    .setParameter("username", username)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }


    @Override
    public User loadUserByUsername(String username){
        return findByUsername(username);
    }
}
