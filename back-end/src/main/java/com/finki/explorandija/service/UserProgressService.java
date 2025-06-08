package com.finki.explorandija.service;

import com.finki.explorandija.model.*;
import com.finki.explorandija.repository.*;
import com.finki.explorandija.exception.AlreadyExistsException;
import com.finki.explorandija.exception.NotFoundException;
import com.finki.explorandija.exception.ValidationException;
import com.finki.explorandija.model.*;
import com.finki.explorandija.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserProgressService {
    private static final Logger logger = LoggerFactory.getLogger(UserProgressService.class);

    private final UserRepository userRepository;
    private final DestinationRepository destinationRepository;
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserDestinationProgressRepository userDestinationProgressRepository;
    private final DiscountRepository discountRepository;
    private final UserDiscountRepository userDiscountRepository;

    @Autowired
    public UserProgressService(UserRepository userRepository, 
                               DestinationRepository destinationRepository, 
                               ChallengeRepository challengeRepository, 
                               UserChallengeRepository userChallengeRepository, 
                               UserDestinationProgressRepository userDestinationProgressRepository,
                               DiscountRepository discountRepository,
                               UserDiscountRepository userDiscountRepository) {
        this.userRepository = userRepository;
        this.destinationRepository = destinationRepository;
        this.challengeRepository = challengeRepository;
        this.userChallengeRepository = userChallengeRepository;
        this.userDestinationProgressRepository = userDestinationProgressRepository;
        this.discountRepository = discountRepository;
        this.userDiscountRepository = userDiscountRepository;
    }

    @Transactional
    public UserDestinationProgress unlockDestination(Long userId, Long destinationId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
        Destination destination = destinationRepository.findById(destinationId)
                .orElseThrow(() -> new NotFoundException("Destination not found with ID: " + destinationId));

        if (userDestinationProgressRepository.existsByUserIdAndDestinationId(userId, destinationId)) {
            throw new AlreadyExistsException("User has already unlocked this destination.");
        }

        UserDestinationProgress progress = new UserDestinationProgress(user, destination);
        return userDestinationProgressRepository.save(progress);
    }

    @Transactional
    public UserChallenge completeChallenge(Long userId, Long challengeId, String submission) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found with ID: " + userId));
        Challenge challenge = challengeRepository.findById(challengeId)
                .orElseThrow(() -> new NotFoundException("Challenge not found with ID: " + challengeId));

        if (!userDestinationProgressRepository.existsByUserIdAndDestinationId(userId, challenge.getDestination().getId())) {
            throw new ValidationException("User must unlock the destination before completing its challenges.");
        }

        if (userChallengeRepository.existsByUserIdAndChallengeId(userId, challengeId)) {
            throw new AlreadyExistsException("User has already completed this challenge.");
        }

        switch (challenge.getType()) {
            case TRIVIA:
            case PUZZLE:
                if (submission == null || submission.trim().isEmpty()) {
                    throw new ValidationException("Submission cannot be empty for TRIVIA or PUZZLE challenges.");
                }
                // Case-insensitive comparison for answers, adjust if needed
                if (!submission.trim().equalsIgnoreCase(challenge.getCorrectAnswer())) {
                    logger.warn("User {} submitted incorrect answer for challenge {}: Submitted: '{}', Expected: '{}'", 
                                user.getUsername(), challenge.getName(), submission, challenge.getCorrectAnswer());
                    throw new ValidationException("Incorrect answer submitted for the challenge.");
                }
                logger.info("User {} submitted correct answer for {} challenge {}: {}", 
                            user.getUsername(), challenge.getType().name(), challenge.getName(), submission);
                break;
            case PHOTO:
                logger.info("User {} completed PHOTO challenge {} (submission: '{}', actual file upload/validation not implemented in this step)", 
                            user.getUsername(), challenge.getName(), submission != null ? submission : "N/A");
                break;
            case ACTION:
            case DISCOVERY:
            case HISTORICAL:
                logger.info("User {} completed {} challenge {} (submission: '{}')", 
                            user.getUsername(), challenge.getType().name(), challenge.getName(), submission != null ? submission : "N/A");
                break;
            default:
                logger.warn("Attempting to complete challenge of unhandled type: {}", challenge.getType());
                throw new ValidationException("Cannot complete challenge of unhandled type: " + challenge.getType());
        }

        UserChallenge userChallenge = new UserChallenge(user, challenge);
        userChallenge = userChallengeRepository.save(userChallenge);

        checkAndUpdateAllChallengesCompleted(user, challenge.getDestination());

        return userChallenge;
    }

    private void checkAndUpdateAllChallengesCompleted(User user, Destination destination) {
        long totalChallengesForDestination = challengeRepository.countByDestinationId(destination.getId());
        long completedChallengesForDestinationByUser = userChallengeRepository.findByUserId(user.getId()).stream()
                .filter(uc -> uc.getChallenge().getDestination().getId().equals(destination.getId()))
                .count();

        if (totalChallengesForDestination > 0 && totalChallengesForDestination == completedChallengesForDestinationByUser) {
            UserDestinationProgress progress = userDestinationProgressRepository.findByUserIdAndDestinationId(user.getId(), destination.getId())
                    .orElseThrow(() -> new NotFoundException("User progress for destination not found. This should not happen."));
            if (!progress.isAllChallengesCompleted()) {
                progress.setAllChallengesCompleted(true);
                userDestinationProgressRepository.save(progress);
                logger.info("User {} completed all challenges for destination {}. Awarding discount(s).", user.getUsername(), destination.getName());
                awardDiscountsForDestination(user, destination);
            }
        }
    }

    private void awardDiscountsForDestination(User user, Destination destination) {
        List<Discount> discountsForDestination = discountRepository.findByDestinationId(destination.getId());
        for (Discount discount : discountsForDestination) {
            if (!userDiscountRepository.existsByUserIdAndDiscountId(user.getId(), discount.getId())) {
                UserDiscount userDiscount = new UserDiscount(user, discount);
                userDiscountRepository.save(userDiscount);
                logger.info("Awarded discount '{}' to user {} for completing destination {}", 
                            discount.getDescription(), user.getUsername(), destination.getName());
            } else {
                logger.info("User {} already has discount '{}' for destination {}. Skipping.", 
                            user.getUsername(), discount.getDescription(), destination.getName());
            }
        }
    }
    
    public List<UserChallenge> getCompletedChallengesForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with ID: " + userId);
        }
        return userChallengeRepository.findByUserId(userId);
    }

    public List<UserDestinationProgress> getUnlockedDestinationsForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with ID: " + userId);
        }
        return userDestinationProgressRepository.findByUserId(userId);
    }

    public List<UserDiscount> getEarnedDiscountsForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found with ID: " + userId);
        }
        return userDiscountRepository.findByUserId(userId);
    }
} 