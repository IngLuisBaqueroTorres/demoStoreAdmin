package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.model.User;
import com.ingeduardo.demostore.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Buscar todos los usuarios
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    // Buscar usuario por ID
    public Optional<User> findUserById(Long id) {
        return userRepository.findById(id);
    }

    // Buscar usuario por email
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Registrar nuevo usuario
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Verificar si email ya existe
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    // Eliminar usuario
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deactivateUser(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    user.setActive(false);
    userRepository.save(user);
}
}
