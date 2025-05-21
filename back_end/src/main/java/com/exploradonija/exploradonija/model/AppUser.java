package com.exploradonija.exploradonija.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @Data
@AllArgsConstructor @NoArgsConstructor
public class AppUser {
    @Id @GeneratedValue
    private Long id;
    private String username;
    private String email;
    private String password;
    private String languagePreference;
    private int totalPoints;

    @OneToMany(mappedBy = "user")
    private List<UserChallengeProgress> challengeProgress;

    @OneToMany(mappedBy = "user")
    private List<UserReward> rewards;
}

