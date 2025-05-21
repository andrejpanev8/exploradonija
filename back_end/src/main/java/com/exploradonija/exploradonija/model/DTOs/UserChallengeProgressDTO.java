package com.exploradonija.exploradonija.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserChallengeProgressDTO {
    private Long challengeId;
    private String challengeTitle;
    private boolean completed;
    private LocalDateTime completedAt;
}

