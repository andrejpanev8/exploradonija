package com.exploradonija.exploradonija.model.Challenges;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity @Data
@DiscriminatorValue("PHOTO")
@AllArgsConstructor @NoArgsConstructor
public class PhotoChallenge extends Challenge {
    private String requiredSubject;
}

