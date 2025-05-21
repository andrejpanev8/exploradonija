package com.exploradonija.exploradonija.controller;

import com.exploradonija.exploradonija.model.DTOs.BusinessPlaceDTO;
import com.exploradonija.exploradonija.model.DTOs.RewardDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/businesses")
public class BusinessPlaceController {

    @GetMapping
    public ResponseEntity<List<BusinessPlaceDTO>> getAllBusinesses() {
        return null;
    }

    @GetMapping("/{id}")
    public ResponseEntity<BusinessPlaceDTO> getBusinessById(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/{id}/rewards")
    public ResponseEntity<List<RewardDTO>> getRewardsForBusiness(@PathVariable Long id) {
        return null;
    }
}

