package com.cinema.demo.service;

import com.cinema.demo.model.User;
import com.cinema.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    // Create
    public User createUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Un utilisateur avec cet email existe déjà");
        }
        return userRepository.save(user);
    }
    
    // Read - tous les utilisateurs
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    // Read - par ID
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    // Read - par email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    // Update
    public User updateUser(Long id, User userDetails) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + id));
        
        user.setNom(userDetails.getNom());
        user.setPrenom(userDetails.getPrenom());
        user.setEmail(userDetails.getEmail());
        user.setPassword(userDetails.getPassword());
        
        return userRepository.save(user);
    }
    
    // Delete
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé avec l'id: " + id));
        userRepository.delete(user);
    }
    
    // Vérifier si l'email existe
    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }
}
