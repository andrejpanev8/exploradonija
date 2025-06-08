package com.finki.explorandija.controller;

import com.finki.explorandija.model.UserChallenge;
import com.finki.explorandija.model.UserDestinationProgress;
import com.finki.explorandija.model.UserDiscount;
import com.finki.explorandija.model.User;
import com.finki.explorandija.service.UserProgressService;
import com.finki.explorandija.dto.ChallengeCompletionRequest;
import com.finki.explorandija.dto.DestinationUnlockRequest;

import com.finki.explorandija.exception.NotFoundException;
import com.finki.explorandija.exception.UnauthorizedException;
import com.finki.explorandija.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/progress")
public class UserProgressController {

    private final UserProgressService userProgressService;
    private final UserService userService; // To get User ID from UserDetails

    @Autowired
    public UserProgressController(UserProgressService userProgressService, UserService userService) {
        this.userProgressService = userProgressService;
        this.userService = userService;
    }

    // Endpoint to unlock a destination
    @PostMapping("/destinations/unlock")
    public ResponseEntity<?> unlockDestination(@RequestBody DestinationUnlockRequest request, 
                                               @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = getCurrentUser(userDetails);
        UserDestinationProgress progress = userProgressService.unlockDestination(currentUser.getId(), request.destinationId());
        return ResponseEntity.ok("Destination unlocked successfully. Progress ID: " + progress.getId());
    }

    // Endpoint to complete a challenge
    @PostMapping("/challenges/complete")
    public ResponseEntity<?> completeChallenge(@RequestBody ChallengeCompletionRequest request, 
                                             @AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = getCurrentUser(userDetails);
        UserChallenge completedChallenge = userProgressService.completeChallenge(
            currentUser.getId(), 
            request.challengeId(), 
            request.submission() // Pass submission data
        );
        return ResponseEntity.ok("Challenge completed successfully. UserChallenge ID: " + completedChallenge.getId());
    }
    
    // Endpoint to get a user's profile data (unlocked destinations and completed challenges)
    @GetMapping("/me")
    public ResponseEntity<?> getMyProgress(@AuthenticationPrincipal UserDetails userDetails) {
        User currentUser = getCurrentUser(userDetails);
        List<UserDestinationProgress> unlockedDestinations = userProgressService.getUnlockedDestinationsForUser(currentUser.getId());
        List<UserChallenge> completedChallenges = userProgressService.getCompletedChallengesForUser(currentUser.getId());
        List<UserDiscount> earnedDiscounts = userProgressService.getEarnedDiscountsForUser(currentUser.getId());
        
        // We can create specific DTOs for this response if needed for cleaner structure
        return ResponseEntity.ok(java.util.Map.of(
            "userId", currentUser.getId(),
            "username", currentUser.getUsername(),
            "unlockedDestinationsCount", unlockedDestinations.size(),
            "completedChallengesCount", completedChallenges.size(),
            "earnedDiscountsCount", earnedDiscounts.size(),
            "unlockedDestinations", unlockedDestinations.stream()
                                        .map(udp -> java.util.Map.of("destinationId", udp.getDestination().getId(), "destinationName", udp.getDestination().getName(), "unlockedAt", udp.getUnlockedAt(), "allChallengesCompleted", udp.isAllChallengesCompleted()))
                                        .collect(Collectors.toList()),
            "completedChallenges", completedChallenges.stream()
                                        .map(uc -> java.util.Map.of("challengeId", uc.getChallenge().getId(), "challengeName", uc.getChallenge().getName(), "destinationId", uc.getChallenge().getDestination().getId(), "completedAt", uc.getCompletedAt()))
                                        .collect(Collectors.toList()),
            "earnedDiscounts", earnedDiscounts.stream()
                                        .map(ud -> java.util.Map.of(
                                            "discountId", ud.getDiscount().getId(),
                                            "description", ud.getDiscount().getDescription(),
                                            "code", ud.getDiscount().getDiscountCode(),
                                            "validUntil", ud.getDiscount().getValidUntil(),
                                            "earnedAt", ud.getEarnedAt(),
                                            "destinationName", ud.getDiscount().getDestination().getName(),
                                            "used", ud.isUsed()
                                        ))
                                        .collect(Collectors.toList())
        ));
    }

    private User getCurrentUser(UserDetails userDetails) {
        if (userDetails == null) {
            throw new UnauthorizedException("User not authenticated");
        }
        return userService.getUserByUsername(userDetails.getUsername())
                .orElseThrow(() -> new NotFoundException("Authenticated user not found in database"));
    }
} 