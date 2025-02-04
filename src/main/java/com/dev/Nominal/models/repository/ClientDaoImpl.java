package com.dev.Nominal.models.repository;

import com.dev.Nominal.models.entity.Client;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ClientDaoImpl implements IClientDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Client> findAll() {
        return em.createQuery("from Client", Client.class).getResultList();
    }

    @Override
    public void save(Client client) {
        if (client.getId() != null && client.getId() > 0) {
            em.merge(client);
        }else
            em.persist(client);

    }

    @Override
    public Client findOne(Long id) {
        return em.find(Client.class, id);
    }

    @Override
    public void delete(Long id) {
        em.remove(findOne(id));
    }

    @Override
    public List<Client> findByNombreOrCiRucContainingIgnoreCase(String term) {
        String query = "SELECT c FROM Client c WHERE LOWER(c.nombre) LIKE LOWER(CONCAT('%', :term, '%')) " +
                "OR LOWER(c.ci_ruc) LIKE LOWER(CONCAT('%', :term, '%'))";
        return em.createQuery(query, Client.class)
                .setParameter("term", term)
                .getResultList();
    }
}
