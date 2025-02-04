package com.dev.Nominal.models.service;

import com.dev.Nominal.models.entity.Personal;
import com.dev.Nominal.models.repository.IPersonalDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PersonalServiceImpl implements IPersonalService {

    @Autowired
    private IPersonalDao personalDao;

    @Transactional(readOnly = true)
    @Override
    public List<Personal> findAll() {
        return personalDao.findAll();
    }
    @Transactional
    @Override
    public void save(Personal personal) {
        personalDao.save(personal);
    }
    @Transactional(readOnly = true)
    @Override
    public Personal findOne(Long id) {
        return personalDao.findOne(id);
    }
    @Transactional
    @Override
    public void delete(Long id) {
        personalDao.delete(id);
    }
}
