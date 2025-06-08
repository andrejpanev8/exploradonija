package com.finki.explorandija.dto;

// Added submission field for quiz answers, puzzle solutions etc.
// For photo uploads, a different mechanism (multipart form data) would typically be used for the request itself,
// but the challengeId might still come through a path variable or a separate JSON part.
public record ChallengeCompletionRequest(Long challengeId, String submission) {
} 