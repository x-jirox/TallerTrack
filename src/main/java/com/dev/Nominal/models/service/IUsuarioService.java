package com.dev.Nominal.models.service;





import com.dev.Nominal.models.entity.User;

import java.util.List;

public interface IUsuarioService {
    public List<User> findAll();
    public void save(User usuario);
    public User findOne(Long id);
    public void delete(Long id);
    public User findByUsername(String username);
    public User loadUserByUsername(String username);
    List<User> findByNombreContainingIgnoreCase(String nombres);
}
