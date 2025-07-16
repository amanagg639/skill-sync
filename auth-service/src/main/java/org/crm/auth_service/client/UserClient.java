package org.crm.auth_service.client;

import org.crm.auth_service.dto.Response;
import org.crm.auth_service.dto.UserLoginRequestDTO;
import org.crm.auth_service.dto.UserLoginResponseDTO;
import org.crm.auth_service.dto.UserRegisterDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", url = "${user.service.url}")
public interface UserClient {

    @PostMapping("/api/v1/user/register")
    ResponseEntity<Response> registerUser(@RequestBody UserRegisterDTO userRegisterDTO);

    @GetMapping("/api/v1/user/test")
    String test();

    @GetMapping("/api/v1/user/test1")
    String test1();

    @PostMapping("/api/v1/user/login")
    UserLoginResponseDTO loginUser(@RequestBody UserLoginRequestDTO userLoginRequestDTO);

    @PostMapping("/api/v1/user/test2")
    String test2(@RequestBody UserLoginRequestDTO userLoginRequestDTO);
}