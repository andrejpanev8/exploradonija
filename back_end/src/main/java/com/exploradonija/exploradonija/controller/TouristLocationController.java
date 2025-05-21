package com.exploradonija.exploradonija.controller;

import com.exploradonija.exploradonija.model.DTOs.ChallengeDTO;
import com.exploradonija.exploradonija.model.DTOs.TouristLocationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
public class TouristLocationController {

    @GetMapping
    public ResponseEntity<List<TouristLocationDTO>> getAllLocations() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<TouristLocationDTO> getLocationById(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/{id}/challenges")
    public ResponseEntity<List<ChallengeDTO>> getChallengesForLocation(@PathVariable Long id) {
        return null;
    }
}