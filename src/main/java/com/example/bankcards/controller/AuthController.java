package com.example.bankcards.controller;

import com.example.bankcards.dto.AuthResponse;
import com.example.bankcards.dto.CreateUserRequest;
import com.example.bankcards.dto.CreateUserResponse;
import com.example.bankcards.dto.LoginRequest;
import com.example.bankcards.dto.RefreshTokenRequest;
import com.example.bankcards.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for authentification
 */

@Slf4j
@RestController
@RequestMapping(path = "/bankcards/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<CreateUserResponse> register(@Valid @RequestBody CreateUserRequest request) {
        log.info("Register " + request.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.registerUser(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authUser(@Valid @RequestBody LoginRequest request) {
        log.info("Login " + request.email());
        return ResponseEntity.ok(authService.authenticateUser(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        log.info("Refresh-token " + request.token());
        return ResponseEntity.ok(authService.refreshToken(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(@AuthenticationPrincipal UserDetails userDetails) {
        authService.logout();
        return ResponseEntity.ok("User logout");
    }

}
