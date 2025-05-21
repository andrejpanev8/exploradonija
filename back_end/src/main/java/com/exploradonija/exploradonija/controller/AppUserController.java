package com.exploradonija.exploradonija.controller;

import com.exploradonija.exploradonija.model.DTOs.AppUserDTO;
import com.exploradonija.exploradonija.model.DTOs.LoginRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class AppUserController {

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody AppUserDTO userDto) {
        return ResponseEntity.ok().body("OK");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok().body("OK");
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<?> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok().body("OK");
    }

    @GetMapping("/{id}/progress")
    public ResponseEntity<?> getUserProgress(@PathVariable Long id) {
        return ResponseEntity.ok().body("OK");
    }
}
