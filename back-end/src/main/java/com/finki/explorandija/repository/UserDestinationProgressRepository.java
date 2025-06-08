package com.finki.explorandija.repository;

import com.finki.explorandija.model.UserDestinationProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserDestinationProgressRepository extends JpaRepository<UserDestinationProgress, Long> {
    Optional<UserDestinationProgress> findByUserIdAndDestinationId(Long userId, Long destinationId);
    List<UserDestinationProgress> findByUserId(Long userId);
    boolean existsByUserIdAndDestinationId(Long userId, Long destinationId);
} 