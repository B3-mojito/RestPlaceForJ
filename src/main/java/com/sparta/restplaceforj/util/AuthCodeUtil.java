package com.sparta.restplaceforj.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;

import java.security.Key;
import java.util.Date;

@Slf4j(topic = "JwtUtil")
@Component
public class AuthCodeUtil {

    public static final String SECRET_KEY = "secret_key ";
    private static final Long EXPIRATION_TIME = 60 * 30L; // 30분 유효기간

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64Utils.decodeFromUrlSafeString(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public String createAuthCode(String email) {
        Date date = new Date();

        return Jwts.builder()
                .setSubject(email)
                .setExpiration(new Date(date.getTime() + EXPIRATION_TIME))
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public static String getEmailFromAuthCode(String authCode) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(authCode)
                .getBody()
                .getSubject();
    }

    public static boolean isTokenExpired(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
            return false;
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}