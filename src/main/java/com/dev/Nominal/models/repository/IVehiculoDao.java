package com.dev.Nominal.models.repository;

import com.dev.Nominal.models.entity.Client;
import com.dev.Nominal.models.entity.User;
import com.dev.Nominal.models.entity.Vehiculo;

import java.util.List;

public interface IVehiculoDao {
    public List<Vehiculo> findAll();
    public void save(Vehiculo vehiculo);
    public Vehiculo findOne(Long id);
    public void delete(Long id);

    public List<Vehiculo> findByMarcaOrModeloOrPlacaOrAnyoContainingIgnoreCase(String term);
}
