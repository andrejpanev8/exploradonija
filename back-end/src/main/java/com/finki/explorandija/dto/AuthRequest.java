package com.finki.explorandija.dto;

// Using records for concise DTOs (Java 14+)
public record AuthRequest(String username, String password) {
} 