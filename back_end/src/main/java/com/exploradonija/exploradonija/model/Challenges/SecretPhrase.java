package com.exploradonija.exploradonija.model.Challenges;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity @Data
@AllArgsConstructor @NoArgsConstructor
@DiscriminatorValue("PHRASE")
public class SecretPhrase extends Challenge {
    private String phrase;
}
