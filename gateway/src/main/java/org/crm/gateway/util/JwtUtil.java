//package com.rituraj.gateway.util;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.SignatureAlgorithm;
//import javax.crypto.spec.SecretKeySpec;
//import java.security.Key;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//
//public class JwtUtil {
//    private static final String SECRET_KEY = "your-256-bit-secret-key-here-32bytes"; // Replace with a secure key
//    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hour
//
//    public static String generateToken(String username, List<String> roles) {
//        Map<String, Object> claims = new HashMap<>();
//        claims.put("roles", roles);
//        return createToken(claims, username);
//    }
//
//    private static String createToken(Map<String, Object> claims, String subject) {
//        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
//        return Jwts.builder()
//                .setClaims(claims)
//                .setSubject(subject)
//                .setIssuedAt(new Date(System.currentTimeMillis()))
//                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
//                .signWith(key, SignatureAlgorithm.HS256)
//                .compact();
//    }
//
//    public static String extractUsername(String token) {
//        return extractClaim(token, Claims::getSubject);
//    }
//
//    public static List<String> extractRoles(String token) {
//        return extractClaim(token, claims -> claims.get("roles", List.class));
//    }
//
//    public static <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
//        final Claims claims = extractAllClaims(token);
//        return claimsResolver.apply(claims);
//    }
//
//    private static Claims extractAllClaims(String token) {
//        Key key = new SecretKeySpec(SECRET_KEY.getBytes(), SignatureAlgorithm.HS256.getJcaName());
//        return Jwts.parserBuilder()
//                .setSigningKey(key)
//                .build()
//                .parseClaimsJws(token)
//                .getBody();
//    }
//
//    public static boolean isTokenValid(String token, String username) {
//        final String extractedUsername = extractUsername(token);
//        return (extractedUsername.equals(username) && !isTokenExpired(token));
//    }
//
//    private static boolean isTokenExpired(String token) {
//        return extractExpiration(token).before(new Date());
//    }
//
//    private static Date extractExpiration(String token) {
//        return extractClaim(token, Claims::getExpiration);
//    }
//}
