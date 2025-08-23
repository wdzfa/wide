package com.wdzfa.wide.service.jwt;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

//    @Value("${jwt}")
//    private String jwtSecret;

    private final String jwtSecret = "jwtsecretofuserauthentication2025";

    public void printSecret() {
        System.out.println("JWT Secret = " + jwtSecret);
    }

    private final long accessTokenExpiration = 1000 * 60 * 15;
    private final Key key = Keys.hmacShaKeyFor(jwtSecret.getBytes());

    public String generateToken(String email){
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String validateToken(String token){
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody()
                    .getSubject();
        } catch (JwtException e){
            return null;
        }
    }


}
