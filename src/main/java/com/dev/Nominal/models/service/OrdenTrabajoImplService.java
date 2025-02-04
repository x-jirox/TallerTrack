package com.dev.Nominal.models.service;

import com.dev.Nominal.models.entity.OrdenTrabajo;
import com.dev.Nominal.models.repository.IOrdenTrabajoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrdenTrabajoImplService implements IOrdenTrabajoService{
    @Autowired
    private IOrdenTrabajoDao ordenTrabajoDao;

    @Transactional(readOnly = true)
    @Override
    public List<OrdenTrabajo> findAll() {
        return ordenTrabajoDao.findAll();
    }

    @Transactional
    @Override
    public void save(OrdenTrabajo ordenTrabajo) {
        ordenTrabajoDao.save(ordenTrabajo);
    }
    @Transactional(readOnly = true)
    @Override
    public OrdenTrabajo findOne(Long id) {
        return ordenTrabajoDao.findOne(id);
    }
    @Transactional(readOnly = true)
    @Override
    public List<OrdenTrabajo> findByClienteNombreOrVehiculoPlacaContainingIgnoreCase(String term) {
        return ordenTrabajoDao.findByClienteNombreOrVehiculoPlacaContainingIgnoreCase(term);
    }
    @Transactional(readOnly = true)
    @Override
    public List<OrdenTrabajo> findByEstado(String estado) {
        return ordenTrabajoDao.findByEstado(estado);
    }

    @Override
    public List<OrdenTrabajo> buscarPorFiltros(String term, LocalDate startDate, LocalDate endDate) {
        return ordenTrabajoDao.buscarPorFiltros(term , startDate,endDate);
    }
}
