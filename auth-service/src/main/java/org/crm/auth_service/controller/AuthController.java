package org.crm.auth_service.controller;

import lombok.RequiredArgsConstructor;
import org.crm.auth_service.dto.Response;
import org.crm.auth_service.dto.UserLoginRequestDTO;
import org.crm.auth_service.dto.UserRegisterDTO;
import org.crm.auth_service.enums.ApiStatus;
import org.crm.auth_service.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody UserRegisterDTO userRegisterDTO){
        return authService.registerUser(userRegisterDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<Response<String>> loginUser(@RequestBody UserLoginRequestDTO userLoginRequestDTO) {
        String token = authService.loginUser(userLoginRequestDTO);

        Response<String> response = Response.<String>builder()
                .status(ApiStatus.CREATED)
                .message("User Logged in successfully!!")
                .data(token)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok(authService.test());
    }

    @GetMapping("/test1")
    public ResponseEntity<String> test1(){
        return ResponseEntity.ok(authService.test1());
    }
}
