package com.exploradonija.exploradonija.model;

import com.exploradonija.exploradonija.model.Challenges.Challenge;
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
public class UserChallengeProgress {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private AppUser user;

    @ManyToOne
    private Challenge challenge;

    private boolean completed;
    private LocalDateTime completedAt;
    private String userInput; // scanned code, phrase, etc.
}

