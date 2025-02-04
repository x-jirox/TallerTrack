package com.dev.Nominal.models.service;

import com.dev.Nominal.models.entity.Vehiculo;

import java.util.List;

public interface IVehiculoService {
    public List<Vehiculo> findAll();
    public void save(Vehiculo vehiculo);
    public Vehiculo findOne(Long id);
    public void delete(Long id);

    //public Vehiculo fichaVehiculo()

    public List<Vehiculo> findByMarcaOrModeloOrPlacaOrAnyoContainingIgnoreCase(String term);
}
