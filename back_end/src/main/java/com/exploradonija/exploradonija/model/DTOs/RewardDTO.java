package com.exploradonija.exploradonija.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RewardDTO {
    private Long id;
    private String description;
    private int requiredPoints;
    private LocalDateTime expiryDate;
    private Long businessId;
}

