package com.archlab.sample08.application.contracts;

public record AuthResponse(String accessToken, String tokenType, long expiresIn) {
}
