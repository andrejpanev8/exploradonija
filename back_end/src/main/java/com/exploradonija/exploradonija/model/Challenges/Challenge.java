package com.exploradonija.exploradonija.model.Challenges;

import com.exploradonija.exploradonija.model.TouristLocation;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity @Data
@AllArgsConstructor
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "challenge_type")
public abstract class Challenge {
    @Id @GeneratedValue
    private Long id;
    private String title;
    private String instructions;
    private int points;

    @ManyToOne
    private TouristLocation location;
}
