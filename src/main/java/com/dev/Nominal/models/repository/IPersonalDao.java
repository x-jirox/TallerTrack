package com.dev.Nominal.models.repository;

import com.dev.Nominal.models.entity.Personal;

import java.util.List;

public interface IPersonalDao {

    public List<Personal> findAll();
    public void save(Personal personal);
    public Personal findOne(Long id);
    public void delete(Long id);
}
