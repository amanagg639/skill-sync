package org.crm.gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.core.HttpHeaders;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final String secretKey;

    public JwtAuthenticationFilter(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, java.io.IOException {

        String path = request.getRequestURI();
        log.info("Traditional JWT Filter - Processing path: {}", path);

        // Bypass JWT validation for public endpoints
        if (isPublicEndpoint(path)) {
            log.info("Bypassing traditional JWT validation for public endpoint: {}", path);
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Traditional JWT Filter - Missing or invalid Authorization header: {}", authHeader);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String jwt = authHeader.substring(7);
        try {
            Claims claims = validateJwt(jwt);
            log.info("Traditional JWT Claims: {}", claims);

            String userId = claims.getSubject();
            String role = claims.get("role", String.class);
            String email = claims.get("email", String.class);
            String userName = claims.get("userName", String.class);

            if (role == null) {
                log.error("Traditional JWT Filter - No 'role' claim found in JWT");
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return;
            }

            // Set Authentication in Security Context
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_" + role));
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    userId, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);

            // Add custom headers for downstream services
            HttpServletRequestWrapper requestWrapper = new HttpServletRequestWrapper(request) {
                @Override
                public String getHeader(String name) {
                    if ("X-User-Id".equals(name)) {
                        return userId;
                    }
                    if ("X-Roles".equals(name)) {
                        return role;
                    }
                    if ("X-Email".equals(name)) {
                        return email;
                    }
                    if ("X-Username".equals(name)) {
                        return userName;
                    }
                    return super.getHeader(name);
                }
            };

            log.info("Traditional JWT validated - userId: {}, role: {}", userId, role);
            filterChain.doFilter(requestWrapper, response);

        } catch (JwtException | IllegalArgumentException e) {
            log.error("Traditional JWT validation failed: {}", e.getMessage(), e);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/api/v1/auth/register") ||
                path.startsWith("/api/v1/auth/login") ||
                path.startsWith("/api/v1/auth/test") ||
                path.startsWith("/api/v1/auth/test1") ||
                path.startsWith("/api/v1/user/test1");
    }

    private Claims validateJwt(String jwt) throws JwtException {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        return Jwts.parser()
                .setSigningKey(signingKey)
                .parseClaimsJws(jwt)
                .getBody();
    }
}