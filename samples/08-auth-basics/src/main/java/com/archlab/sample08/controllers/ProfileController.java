package com.archlab.sample08.controllers;

import com.archlab.sample08.application.contracts.UserProfileResponse;
import com.archlab.sample08.domain.AppUser;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProfileController {
    @GetMapping("/api/profile")
    @PreAuthorize("isAuthenticated()")
    public UserProfileResponse profile(Authentication authentication) {
        var user = (AppUser) authentication.getPrincipal();
        return new UserProfileResponse(user.getId(), user.getEmail(), user.getFullName());
    }
}
