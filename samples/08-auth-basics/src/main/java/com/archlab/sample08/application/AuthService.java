package com.archlab.sample08.application;

import com.archlab.sample08.application.contracts.AuthResponse;
import com.archlab.sample08.application.contracts.LoginRequest;
import com.archlab.sample08.application.contracts.RegisterRequest;
import com.archlab.sample08.application.contracts.UserProfileResponse;
import com.archlab.sample08.config.JwtConfig;
import com.archlab.sample08.domain.AppUser;
import com.archlab.sample08.security.JwtTokenProvider;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtConfig jwtConfig;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtTokenProvider jwtTokenProvider, JwtConfig jwtConfig) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtConfig = jwtConfig;
    }

    public UserProfileResponse register(RegisterRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);
        userRepository.findByEmail(normalizedEmail).ifPresent(existing -> {
            throw new IllegalArgumentException("Email is already registered.");
        });

        var user = new AppUser();
        user.setEmail(normalizedEmail);
        user.setFullName(request.fullName().trim());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        var saved = userRepository.save(user);

        return new UserProfileResponse(saved.getId(), saved.getEmail(), saved.getFullName());
    }

    public AuthResponse login(LoginRequest request) {
        String normalizedEmail = request.email().trim().toLowerCase(Locale.ROOT);
        var user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new IllegalArgumentException("Invalid credentials."));

        if (!passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new IllegalArgumentException("Invalid credentials.");
        }

        return new AuthResponse(jwtTokenProvider.generateToken(user), "Bearer", jwtConfig.getExpirationMs() / 1000);
    }
}
