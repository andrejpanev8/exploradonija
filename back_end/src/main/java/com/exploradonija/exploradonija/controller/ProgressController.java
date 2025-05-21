package com.exploradonija.exploradonija.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/progress")
public class ProgressController {

    @GetMapping("/user/{userId}/locations")
    public ResponseEntity<?> getCompletedLocations(@PathVariable Long userId) {
        return ResponseEntity.ok().body("OK");
    }

    @GetMapping("/user/{userId}/challenges")
    public ResponseEntity<?> getCompletedChallenges(@PathVariable Long userId) {
        return ResponseEntity.ok().body("OK");
    }
}

