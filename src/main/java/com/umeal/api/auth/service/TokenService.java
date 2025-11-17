package com.umeal.api.auth.service;

import com.umeal.api.exception.BaseApiException;
import com.umeal.api.user.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;
    
    @Value("${api.security.token.expiration-hours}")
    private long expirationHours;

    public String generateToken(UserDetails userDetails) {
        Instant expirationTime = LocalDateTime.now().plusHours(expirationHours).toInstant(ZoneOffset.of("-03:00"));

        return Jwts.builder()
                .setIssuer("umeal-api")
                .setSubject(userDetails.getUsername())
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getSubjectFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.getSubject();
            
        } catch (Exception e) {

            throw new RuntimeException("Token JWT inválido ou expirado.", e);
        }
    }


    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}