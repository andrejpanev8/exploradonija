package com.exploradonija.exploradonija.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TouristLocationDTO {
    private Long id;
    private String name;
    private String description;
    private String region;
    private String coordinates;
    private String imageUrl;
}

