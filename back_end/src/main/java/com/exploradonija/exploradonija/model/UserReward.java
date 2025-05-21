package com.exploradonija.exploradonija.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity @Data
@AllArgsConstructor @NoArgsConstructor
public class UserReward {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private Reward reward;

    private boolean redeemed;
    private LocalDateTime redeemedAt;
}

