package com.finki.explorandija.service;

import com.finki.explorandija.model.Challenge;
import com.finki.explorandija.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChallengeService {

    private final ChallengeRepository challengeRepository;

    @Autowired
    public ChallengeService(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    public List<Challenge> getAllChallenges() {
        return challengeRepository.findAll();
    }

    public Optional<Challenge> getChallengeById(Long id) {
        return challengeRepository.findById(id);
    }

    public List<Challenge> getChallengesByDestinationId(Long destinationId) {
        return challengeRepository.findByDestinationId(destinationId);
    }

    public Challenge createChallenge(Challenge challenge) {
        // Add validation or other business logic if needed
        return challengeRepository.save(challenge);
    }

    public Challenge updateChallenge(Long id, Challenge updatedChallenge) {
        // Add validation or other business logic if needed
        if (challengeRepository.existsById(id)) {
            updatedChallenge.setId(id);
            return challengeRepository.save(updatedChallenge);
        } else {
            // Handle challenge not found
            return null;
        }
    }

    public void deleteChallenge(Long id) {
        challengeRepository.deleteById(id);
    }
} 