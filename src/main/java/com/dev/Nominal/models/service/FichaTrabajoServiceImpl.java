package com.dev.Nominal.models.service;

import com.dev.Nominal.models.entity.FichaTrabajo;
import com.dev.Nominal.models.repository.IFichaTrabajoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class FichaTrabajoServiceImpl implements IFichaTrabajoService {

    @Autowired
    private IFichaTrabajoDao fichaTrabajoDao;


    @Transactional(readOnly = true)
    @Override
    public List<FichaTrabajo> findAll() {
        return fichaTrabajoDao.findAll();
    }

    @Transactional
    @Override
    public void save(FichaTrabajo fichaTrabajo) {
        try {
            fichaTrabajoDao.save(fichaTrabajo);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la ficha: " + e.getMessage(), e);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public FichaTrabajo findOne(Long id) {
        return fichaTrabajoDao.findOne(id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
    fichaTrabajoDao.delete(id);
    }

    @Override
    public List<FichaTrabajo> findByPlaca(String term) {
        return fichaTrabajoDao.findByPlaca(term);
    }
}
