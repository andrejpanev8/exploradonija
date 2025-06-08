package com.finki.explorandija.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description; // e.g., "10% off at Restaurant X", "Free coffee at Cafe Y"

    @Column(unique = true, nullable = false) // Each discount code should be unique
    private String discountCode;

    private LocalDate validUntil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false)
    private Destination destination; // The destination this discount is associated with
} 