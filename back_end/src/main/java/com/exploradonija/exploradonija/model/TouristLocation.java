package com.exploradonija.exploradonija.model;

import com.exploradonija.exploradonija.model.Challenges.Challenge;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @Data
@AllArgsConstructor @NoArgsConstructor
public class TouristLocation {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String description;
    private String region;
    private String coordinates;
    private String imageUrl;

    @OneToMany(mappedBy = "location")
    private List<Challenge> challenges;

    @OneToMany(mappedBy = "location")
    private List<BusinessPlace> businesses;
}
