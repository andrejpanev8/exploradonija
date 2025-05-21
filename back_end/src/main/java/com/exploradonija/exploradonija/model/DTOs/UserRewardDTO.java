package com.exploradonija.exploradonija.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRewardDTO {
    private Long id;
    private Long rewardId;
    private String rewardDescription;
    private boolean redeemed;
    private LocalDateTime redeemedAt;
}

