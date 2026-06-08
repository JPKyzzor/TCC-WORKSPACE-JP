package com.jpk.backTCC.controller;

import com.jpk.backTCC.dto.LoginRequestDTO;
import com.jpk.backTCC.dto.LoginResponseDTO;
import com.jpk.backTCC.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO payload) {
        return ResponseEntity.ok(authService.login(payload));
    }
}
