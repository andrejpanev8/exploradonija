package com.finki.explorandija.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_discounts", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "discount_id"}) // A user can earn a specific discount only once
})
@Data
public class UserDiscount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id", nullable = false)
    private Discount discount;

    @Column(nullable = false)
    private LocalDateTime earnedAt;

    private boolean used; // To track if the discount has been redeemed
    private LocalDateTime usedAt;

    public UserDiscount() {
    }

    public UserDiscount(User user, Discount discount) {
        this.user = user;
        this.discount = discount;
        this.earnedAt = LocalDateTime.now();
        this.used = false;
    }
} 