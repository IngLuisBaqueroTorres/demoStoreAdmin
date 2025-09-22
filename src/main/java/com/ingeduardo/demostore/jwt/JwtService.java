package com.ingeduardo.demostore.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

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
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

  public String generateToken(User user) {
    Map<String, Object> claims = new HashMap<>();

    List<String> roles = user.getRoles().stream()
            .map(role -> role.getName().name())
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
        return extractClaim(token, claims -> claims.getExpiration()).before(new Date());
    }

    private <T> T extractClaim(String token, Function<io.jsonwebtoken.Claims, T> claimsResolver) {
        var claims = Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody();
        return claimsResolver.apply(claims);
    }

    public List<String> extractRoles(String token) {
    Claims claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();

    return claims.get("roles", List.class);
}

}
