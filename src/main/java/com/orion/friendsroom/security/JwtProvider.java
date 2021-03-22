package com.orion.friendsroom.security;

import com.orion.friendsroom.entity.RoleEntity;
import com.orion.friendsroom.exceptions.JwtAuthenticationException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class JwtProvider {

    @Value("${jwt.token.security}")
    private String secret;

    public String generateToken(String email) {
        Date date = Date.from(LocalDate.now().plusDays(14).atStartOfDay(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(date)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token);

            if (claims.getBody().getExpiration().before(new Date())) {
                return false;
            }

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            throw new JwtAuthenticationException("JWT token is expired or invalid");
        }
    }

    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }
}
