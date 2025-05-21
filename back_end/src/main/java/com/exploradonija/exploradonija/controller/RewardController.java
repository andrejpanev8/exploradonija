package com.exploradonija.exploradonija.controller;

import com.exploradonija.exploradonija.model.DTOs.RewardDTO;
import com.exploradonija.exploradonija.model.DTOs.UserRewardDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rewards")
public class RewardController {

    @GetMapping
    public ResponseEntity<List<RewardDTO>> getAllRewards() {
        return null;
    }

    @PostMapping("/{id}/redeem")
    public ResponseEntity<?> redeemReward(@PathVariable Long id, @RequestParam Long userId) {
        return null;
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserRewardDTO>> getUserRewards(@PathVariable Long userId) {
        return null;
    }
}

