package com.dev.Nominal.models.service;

import com.dev.Nominal.models.entity.Client;
import com.dev.Nominal.models.repository.IClientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClientServiceImpl implements IClienteService{

    @Autowired
    private IClientDao clientDao;

    @Transactional(readOnly = true)
    @Override
    public List<Client> findAll() {
        return clientDao.findAll();
    }

    @Transactional
    @Override
    public void save(Client client) {
        clientDao.save(client);
    }

    @Transactional(readOnly = true)
    @Override
    public Client findOne(Long id) {
        return clientDao.findOne(id);
    }
    @Transactional
    @Override
    public void delete(Long id) {
        clientDao.delete(id);

    }

    @Transactional(readOnly = true)
    @Override
    public List<Client> findByNombreOrCiRucContainingIgnoreCase(String term) {
        return clientDao.findByNombreOrCiRucContainingIgnoreCase(term);
    }
}
