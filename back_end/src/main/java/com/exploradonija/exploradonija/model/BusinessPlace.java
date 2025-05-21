package com.exploradonija.exploradonija.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity @Data
@AllArgsConstructor @NoArgsConstructor
public class BusinessPlace {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String type;
    private String contactInfo;
    private String address;

    @ManyToOne
    private TouristLocation location;

    @OneToMany(mappedBy = "business")
    private List<Reward> rewards;
}

