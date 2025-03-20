package com.example.demo.service;

import com.example.demo.exaptions.JwtAuthenticationException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service
public class JwtUtils {

    public SecretKey getKey() {
        String secretKey = "c29tZS1oZXJlLXNvbWUtdGVzdC1rZXktdmFsdWUc29tZS1oZXJlLXNvbWUtdGVzdC1rZXktdmFsdWUc29tZS1oZXJlLXNvbWUtdGVzdC1rZXktdmFsdWUc29tZS1oZXJlLXNvbWUtdGVzdC1rZXktdmFsdWUc29tZS1oZXJlLXNvbWUtdGVzdC1rZXktdmFsdWU";
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8); // Directly use the string as bytes
        return Keys.hmacShaKeyFor(keyBytes); // Use the key for HMAC
    }


    public String generateToken(Authentication user ) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles",user.getAuthorities());
        return Jwts.builder().signWith(getKey()).addClaims(claims).setSubject(user.getName()).setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hour expiration
                .compact();
    }

    public String extractUsername(String token) {
         return extractClaims(token).getSubject();
    }

    public  Boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date(System.currentTimeMillis()));
    }


    public Boolean  isTokenValid (String token , UserDetails user) {
        return !isTokenExpired(token) && user.getUsername().equals(extractUsername(token));
    }


    public Claims extractClaims(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
        } catch (UnsupportedJwtException e) {
            throw new JwtAuthenticationException("Invalid JWT: Unsupported algorithm or incorrect key");
        } catch (MalformedJwtException e) {
            throw new JwtAuthenticationException("Invalid JWT: Malformed token");
        } catch (ExpiredJwtException e) {
            throw new JwtAuthenticationException("Invalid JWT: Token has expired");
        } catch (SignatureException e) {
            throw new JwtAuthenticationException("Invalid JWT: Signature verification failed");
        } catch (IllegalArgumentException e) {
            throw new JwtAuthenticationException("Invalid JWT: Token cannot be null or empty");
        }
    }
}
