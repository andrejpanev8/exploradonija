package com.finki.explorandija.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_destination_progress", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "destination_id"})
})
@Data
public class UserDestinationProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination;

    @Column(nullable = false)
    private LocalDateTime unlockedAt;

    private boolean allChallengesCompleted; // Could be updated by a service method

    public UserDestinationProgress() {
    }

    public UserDestinationProgress(User user, Destination destination) {
        this.user = user;
        this.destination = destination;
        this.unlockedAt = LocalDateTime.now();
        this.allChallengesCompleted = false;
    }
} 