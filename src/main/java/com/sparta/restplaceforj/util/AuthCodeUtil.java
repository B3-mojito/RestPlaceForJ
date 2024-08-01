package com.sparta.restplaceforj.util;

import com.amazonaws.util.Base64;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class AuthCodeUtil {

    public static final String SECRET_KEY = "secret_key ";
    public static final Long EXPIRATION_TIME = 30 * 60 * 1000L; // 30분 유효기간

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAuthCode(String email) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(date.getTime() + EXPIRATION_TIME))
                .setIssuedAt(date)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String getEmailFromAuthCode(String authCode) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(authCode)
                .getBody()
                .getSubject();
    }
}