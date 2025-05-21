package com.exploradonija.exploradonija.controller;

import com.exploradonija.exploradonija.model.DTOs.ChallengeDTO;
import com.exploradonija.exploradonija.model.DTOs.ChallengeSubmissionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
public class ChallengeController {

    @GetMapping("/{id}")
    public ResponseEntity<ChallengeDTO> getChallengeById(@PathVariable Long id) {
        return null;
    }

    @PostMapping("/{id}/submit")
    public ResponseEntity<?> submitChallengeResponse(
            @PathVariable Long id,
            @RequestBody ChallengeSubmissionDTO submission) {
        return null;
    }

    @GetMapping("/types")
    public ResponseEntity<List<String>> getAvailableChallengeTypes() {
        return null;
    }
}

