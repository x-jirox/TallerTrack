package com.dev.Nominal.models.service;

import com.dev.Nominal.models.entity.Vehiculo;
import com.dev.Nominal.models.repository.IUsuarioDao;
import com.dev.Nominal.models.repository.IVehiculoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VehiculoServiceImpl implements IVehiculoService{

    @Autowired
    private IVehiculoDao vehiculoDao;

    @Transactional(readOnly = true)
    @Override
    public List<Vehiculo> findAll() {
        return vehiculoDao.findAll();
    }

    @Transactional
    @Override
    public void save(Vehiculo vehiculo) {
        try {
            vehiculoDao.save(vehiculo);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el vehiculo: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Vehiculo findOne(Long id) {
        return vehiculoDao.findOne(id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
     vehiculoDao.delete(id);
    }

    @Override
    public List<Vehiculo> findByMarcaOrModeloOrPlacaOrAnyoContainingIgnoreCase(String term) {
        return vehiculoDao.findByMarcaOrModeloOrPlacaOrAnyoContainingIgnoreCase(term);
    }
}
