package org.crm.auth_service.util;

import org.crm.auth_service.enums.Role;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.expiration.ms}")
    private long expirationMs;

    public String generateToken(String userId, Role role, String email, String userName) {

        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");

        return Jwts.builder()
                .setSubject(userId)
                .claim("role", role.name())
                .claim("email", email)
                .claim("userName", userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
