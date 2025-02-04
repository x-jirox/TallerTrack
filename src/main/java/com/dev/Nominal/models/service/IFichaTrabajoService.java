package com.dev.Nominal.models.service;

import com.dev.Nominal.models.entity.FichaTrabajo;

import java.util.List;

public interface IFichaTrabajoService {
    public List<FichaTrabajo> findAll();
    public void save(FichaTrabajo fichaTrabajo);
    public FichaTrabajo findOne(Long id);
    public void delete(Long id);

    public List<FichaTrabajo> findByPlaca(String term);
}
