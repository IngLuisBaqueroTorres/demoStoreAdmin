package com.ingeduardo.demostore.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.nio.charset.StandardCharsets;

import com.ingeduardo.demostore.model.Role;
import com.ingeduardo.demostore.model.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final long EXPIRATION = 1000 * 60 * 60 * 10; // 10 horas
    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    private final Key key;

    public JwtService(@Value("${app.jwt.secret:}") String secret) {
        if (secret == null || secret.isBlank()) {
            logger.warn("No JWT secret configured (app.jwt.secret); generating ephemeral key. Tokens won't survive restarts.");
            this.key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        } else {
            byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
            this.key = Keys.hmacShaKeyFor(keyBytes);
        }
    }

  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();

    List<String> roles = user.getRoles().stream()
            .map(Role::getName)
            .collect(Collectors.toList());

    claims.put("roles", roles);

    return Jwts.builder()
            .setClaims(claims)
            .setSubject(user.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
            .signWith(key)
            .compact();
}


    public String extractUsername(String token) {
        return extractClaim(token, claims -> claims.getSubject());
    }

    public boolean isTokenValid(String token, String userEmail) {
        return extractUsername(token).equals(userEmail) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        var claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }
}
