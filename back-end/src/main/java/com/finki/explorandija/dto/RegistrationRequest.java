package com.finki.explorandija.dto;

// Could add more fields like email, firstName, lastName if needed
public record RegistrationRequest(String username, String password) {
} 