package com.finki.explorandija.service;

import com.finki.explorandija.model.User;
import com.finki.explorandija.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder; // Will be injected via setter or constructor

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Spring Security needs a PasswordEncoder to be available in this service
    // We can inject it via setter or constructor. Let's use a setter for now.
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        
        // Spring Security UserDetails needs roles/authorities. For now, using an empty list.
        // You might want to add a Role entity and a roles field to your User entity.
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User createUser(User user) {
        if (passwordEncoder == null) {
            throw new IllegalStateException("PasswordEncoder not set in UserService. Ensure it is properly injected.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        if (userRepository.existsById(id)) {
            updatedUser.setId(id);
            // Handle password update carefully - only if a new password is provided and re-hash it
            if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
                 if (passwordEncoder == null) {
                    throw new IllegalStateException("PasswordEncoder not set in UserService. Ensure it is properly injected.");
                }
                updatedUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
            } else {
                // If password is not being updated, keep the old one
                User existingUser = userRepository.findById(id).orElse(null);
                if (existingUser != null) {
                    updatedUser.setPassword(existingUser.getPassword());
                }
            }
            return userRepository.save(updatedUser);
        } else {
            return null; // Handle user not found
        }
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Additional methods for user-specific logic, e.g., unlocking destinations
} 