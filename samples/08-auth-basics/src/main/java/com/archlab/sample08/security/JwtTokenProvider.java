package com.archlab.sample08.security;

import com.archlab.sample08.config.JwtConfig;
import com.archlab.sample08.domain.AppUser;
import io.jsonwebtoken.Jwts;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider {
    private final JwtConfig jwtConfig;

    public JwtTokenProvider(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }

    public String generateToken(AppUser user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusMillis(jwtConfig.getExpirationMs());

        return Jwts.builder()
                .subject(user.getId())
                .claim("email", user.getEmail())
                .claim("fullName", user.getFullName())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(jwtConfig.signingKey())
                .compact();
    }

    public boolean validateToken(String token) {
        Jwts.parser().verifyWith(jwtConfig.signingKey()).build().parseSignedClaims(token);
        return true;
    }

    public Authentication getAuthentication(String token) {
        var claims = Jwts.parser().verifyWith(jwtConfig.signingKey()).build().parseSignedClaims(token).getPayload();
        var user = new AppUser();
        user.setId(claims.getSubject());
        user.setEmail(claims.get("email", String.class));
        user.setFullName(claims.get("fullName", String.class));

        return new UsernamePasswordAuthenticationToken(
                user,
                token,
                List.of(new SimpleGrantedAuthority("ROLE_USER")));
    }
}
