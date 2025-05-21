package com.exploradonija.exploradonija.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity @Data
@AllArgsConstructor @NoArgsConstructor
public class Reward {
    @Id @GeneratedValue
    private Long id;
    private String description;
    private int requiredPoints;
    private LocalDateTime expiryDate;

    @ManyToOne
    private BusinessPlace business;
}

