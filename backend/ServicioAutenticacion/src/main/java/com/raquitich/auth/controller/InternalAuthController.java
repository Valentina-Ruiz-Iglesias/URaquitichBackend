package com.raquitich.auth.controller;

import com.raquitich.auth.dto.InternalCreateUserRequest;
import com.raquitich.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Endpoints de uso EXCLUSIVAMENTE interno entre microservicios.
 * No deben estar expuestos al frontend ni a internet.
 * Se protegen mediante una API key compartida (X-Internal-API-Key).
 */
@RestController
@RequestMapping("/internal")
public class InternalAuthController {

    @Value("${app.internal.api-key}")
    private String internalApiKey;

    private final AuthService authService;

    public InternalAuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/users/create")
    public ResponseEntity<Map<String, String>> createUser(
            @RequestHeader(value = "X-Internal-API-Key", required = false) String apiKey,
            @Valid @RequestBody InternalCreateUserRequest request
    ) {
        if (apiKey == null || !internalApiKey.equals(apiKey)) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "Acceso no autorizado"));
        }

        authService.createInternalUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of("message", "Usuario creado exitosamente", "username", request.getUsername()));
    }
}
