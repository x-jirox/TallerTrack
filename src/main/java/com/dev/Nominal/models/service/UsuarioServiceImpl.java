package com.dev.Nominal.models.service;


import com.dev.Nominal.models.entity.User;
import com.dev.Nominal.models.repository.IUsuarioDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private IUsuarioDao usuarioDao;


    @Transactional(readOnly = true)
    @Override
    public List<User> findAll() {
        return usuarioDao.findAll();
    }
    @Transactional
    @Override
    public void save(User usuario) {
        try {
            usuarioDao.save(usuario);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar el usuario: " + e.getMessage(), e);
        }
    }
    @Transactional(readOnly = true)
    @Override
    public User findOne(Long id) {
        return usuarioDao.findOne(id);
    }

    @Transactional
    @Override
    public void delete(Long id) {
        usuarioDao.delete(id);
    }
    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {
        return usuarioDao.findByUsername(username);
    }
    @Transactional(readOnly = true)
    @Override
    public User loadUserByUsername(String username) {
        return usuarioDao.loadUserByUsername(username);
    }
    @Transactional(readOnly = true)
    @Override
    public List<User> findByNombreContainingIgnoreCase(String nombres) {
        return usuarioDao.findByNombreContainingIgnoreCase(nombres);
    }
}
