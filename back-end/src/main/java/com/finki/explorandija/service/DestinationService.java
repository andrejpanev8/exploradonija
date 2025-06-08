package com.finki.explorandija.service;

import com.finki.explorandija.model.Destination;
import com.finki.explorandija.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DestinationService {

    private final DestinationRepository destinationRepository;

    @Autowired
    public DestinationService(DestinationRepository destinationRepository) {
        this.destinationRepository = destinationRepository;
    }

    public List<Destination> getAllDestinations() {
        return destinationRepository.findAll();
    }

    public Optional<Destination> getDestinationById(Long id) {
        return destinationRepository.findById(id);
    }

    public Destination createDestination(Destination destination) {
        return destinationRepository.save(destination);
    }

    public Destination updateDestination(Long id, Destination updatedDestination) {
        if (destinationRepository.existsById(id)) {
            updatedDestination.setId(id);
            return destinationRepository.save(updatedDestination);
        } else {
            return null;
        }
    }

    public void deleteDestination(Long id) {
        destinationRepository.deleteById(id);
    }
} 