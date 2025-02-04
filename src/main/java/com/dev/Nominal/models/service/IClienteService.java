package com.dev.Nominal.models.service;

import com.dev.Nominal.models.entity.Client;

import java.util.List;

public interface IClienteService {
    public List<Client> findAll();
    public void save(Client client);
    public Client findOne(Long id);
    public void delete(Long id);
    public List<Client> findByNombreOrCiRucContainingIgnoreCase(String term);
}
