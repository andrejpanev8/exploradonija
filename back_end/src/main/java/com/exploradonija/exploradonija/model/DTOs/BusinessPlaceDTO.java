package com.exploradonija.exploradonija.model.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BusinessPlaceDTO {
    private Long id;
    private String name;
    private String type;
    private String contactInfo;
    private String address;
    private Long locationId;
}

