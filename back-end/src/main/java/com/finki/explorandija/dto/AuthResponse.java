package com.finki.explorandija.dto;

public record AuthResponse(String jwt, String username, Long userId) {
    // We can add more user details here if needed on the frontend after login
} 