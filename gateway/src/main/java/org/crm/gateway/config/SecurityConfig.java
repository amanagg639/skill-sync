package org.crm.gateway.config;

import org.crm.gateway.util.JwtAuthenticationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authz -> authz
                        .requestMatchers("/api/v1/auth/register", "/api/v1/auth/login",
                                "/api/v1/auth/test", "/api/v1/auth/test1",
                                "/api/v1/user/test1").permitAll()
                        .requestMatchers("/api/v1/auth/test2", "/api/v1/user/test3").hasRole("STUDENT")
                        .requestMatchers("/instructor/**").hasRole("INSTRUCTOR")
                        .requestMatchers("/batch/**").hasAnyRole("STUDENT", "INSTRUCTOR")
                        .anyRequest().authenticated()
                )
                // OAuth2 Resource Server Configuration
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder())
                                .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())
                        )
                )
                // Add custom JWT filter before OAuth2 filter
                .addFilterBefore(new JwtAuthenticationFilter(secretKey),
                        BearerTokenAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        SecretKey secretKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();

        decoder.setJwtValidator(new DelegatingOAuth2TokenValidator<>(
                JwtValidators.createDefault(),
                token -> {
                    try {
                        log.info("OAuth2 JWT Validation: {}", token.getTokenValue());
                        String role = token.getClaimAsString("role");
                        if (role == null) {
                            log.error("Missing 'role' claim in JWT");
                            return OAuth2TokenValidatorResult.failure(
                                    new OAuth2Error("invalid_token", "Missing 'role' claim", null));
                        }
                        return OAuth2TokenValidatorResult.success();
                    } catch (Exception e) {
                        log.error("OAuth2 JWT validation failed: {}", e.getMessage(), e);
                        return OAuth2TokenValidatorResult.failure(
                                new OAuth2Error("invalid_token", e.getMessage(), null));
                    }
                }
        ));
        return decoder;
    }
}