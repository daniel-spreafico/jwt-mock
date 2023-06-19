package io.spreafico.jwtmock.controllers;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JwtController {

    @Value("${jwt.secret}")
    private String secret;

    @GetMapping("/")
    public String dynamicBuilderGeneric(@RequestParam Map<String, String> params) {
        // @formatter:off
        Instant now = Instant.now();
        JwtBuilder builder = Jwts.builder();
        params.forEach(builder::claim);
        long expirationMinutes = Long.parseLong(params.getOrDefault("expirationMinutes", "60"));
        return builder.setId(UUID.randomUUID().toString())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(now.plus(expirationMinutes, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, getSecret())
                .compact();
        // @formatter:on
    }

    private String getSecret() {
        return new String(Base64.getEncoder().encode(secret.getBytes()));
    }
}
