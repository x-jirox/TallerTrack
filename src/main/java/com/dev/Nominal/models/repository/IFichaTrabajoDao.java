package com.dev.Nominal.models.repository;

import com.dev.Nominal.models.entity.Client;
import com.dev.Nominal.models.entity.FichaTrabajo;

import java.util.List;

public interface IFichaTrabajoDao {

    public List<FichaTrabajo> findAll();
    public void save(FichaTrabajo fichaTrabajo);
    public FichaTrabajo findOne(Long id);
    public void delete(Long id);

    public List<FichaTrabajo> findByPlaca(String term);
}
