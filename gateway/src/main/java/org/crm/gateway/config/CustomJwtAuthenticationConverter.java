package org.crm.gateway.config;

import com.fasterxml.jackson.databind.util.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collections;
import java.util.List;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {
    private static final Logger log = LoggerFactory.getLogger(CustomJwtAuthenticationConverter.class);

    @Override
    public AbstractAuthenticationToken convert(Jwt jwt) {
        log.info("OAuth2 Converting JWT to AuthenticationToken: {}", jwt.getClaims());
        String role = jwt.getClaimAsString("role");
        List<SimpleGrantedAuthority> authorities = role != null
                ? Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role))
                : Collections.emptyList();
        return new JwtAuthenticationToken(jwt, authorities, jwt.getSubject());
    }
}