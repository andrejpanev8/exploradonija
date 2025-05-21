package com.exploradonija.exploradonija.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChallengeSubmissionDTO {
    private Long userId;
    private String response;
    private String imageUrl;
}

