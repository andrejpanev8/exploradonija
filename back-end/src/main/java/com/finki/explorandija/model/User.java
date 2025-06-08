package com.finki.explorandija.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore; // To prevent recursion in serialization

@Entity
@Table(name = "app_user")
@Data// Renamed table to avoid conflict with "USER" keyword in some SQL dbs
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    @JsonIgnore // Don't send password to frontend
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // Avoid sending all progress details with basic user, fetch on demand
    private Set<UserDestinationProgress> destinationProgresses;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<UserChallenge> completedChallenges;

} 