package com.dev.Nominal.models.repository;



import com.dev.Nominal.models.entity.User;

import java.util.List;

public interface IUsuarioDao {
    public List<User> findAll();
    public void save(User usuario);
    public User findOne(Long id);
    public void delete(Long id);
    List<User> findByNombreContainingIgnoreCase(String nombres);
    public User findByUsername(String username);
    public User loadUserByUsername(String username);
}
