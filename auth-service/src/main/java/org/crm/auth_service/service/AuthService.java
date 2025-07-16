package org.crm.auth_service.service;

import lombok.RequiredArgsConstructor;
import org.crm.auth_service.client.UserClient;
import org.crm.auth_service.dto.Response;
import org.crm.auth_service.dto.UserLoginRequestDTO;
import org.crm.auth_service.dto.UserLoginResponseDTO;
import org.crm.auth_service.dto.UserRegisterDTO;
import org.crm.auth_service.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserClient userClient;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public ResponseEntity<Response> registerUser(UserRegisterDTO userRegisterDTO){
        userRegisterDTO.setPassword(passwordEncoder.encode(userRegisterDTO.getPassword()));
        return userClient.registerUser(userRegisterDTO);
    }

    public String test() {
        return userClient.test();
    }

    public String test1() {
        return userClient.test1();
    }

    public String loginUser(UserLoginRequestDTO userLoginRequestDTO) {
        UserLoginResponseDTO userLoginResponseDTO = userClient.loginUser(userLoginRequestDTO);
        return jwtUtil.generateToken(
                userLoginResponseDTO.getUserId(),
                userLoginResponseDTO.getRole(),
                userLoginResponseDTO.getEmail(),
                userLoginResponseDTO.getUserName()
        );
    }

    public String test2(UserLoginRequestDTO userLoginRequestDTO) {
        return userClient.test2(userLoginRequestDTO);
    }
}