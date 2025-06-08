package com.finki.explorandija.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.finki.explorandija.model.enums.ChallengeType;

@Entity
@Data
@NoArgsConstructor
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    private ChallengeType type;
    
    @Column(nullable = true)
    private String correctAnswer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    @JsonBackReference // To handle serialization with Destination.challenges
    private Destination destination;

    @OneToMany(mappedBy = "challenge", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore // Usually not needed to send all user completions when fetching a challenge
    private Set<UserChallenge> userCompletions;

    public Challenge(String name, String description, ChallengeType type, Destination destination) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.destination = destination;
    }
} 