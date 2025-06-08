package com.finki.explorandija.repository;

import com.finki.explorandija.model.Challenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Long> {
    List<Challenge> findByDestinationId(Long destinationId);
    long countByDestinationId(Long destinationId);
} 