package com.raquitich.auth.controller;

import com.raquitich.auth.dto.AdminCreateUserRequest;
import com.raquitich.auth.dto.ChangeRoleRequest;
import com.raquitich.auth.dto.UserResponse;
import com.raquitich.auth.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/usuarios")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:3000"})
public class AdminUserController {

    private final AuthService authService;

    public AdminUserController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN')")
    public ResponseEntity<List<UserResponse>> listar() {
        return ResponseEntity.ok(authService.listarUsuarios());
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN')")
    public ResponseEntity<UserResponse> crear(@RequestBody AdminCreateUserRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.crearUsuarioAdmin(request));
    }

    @PutMapping("/{id}/rol")
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN')")
    public ResponseEntity<UserResponse> cambiarRol(
            @PathVariable Long id,
            @RequestBody ChangeRoleRequest request) {
        return ResponseEntity.ok(authService.cambiarRol(id, request));
    }

    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('DIRECTIVO', 'ADMIN')")
    public ResponseEntity<UserResponse> cambiarEstado(
            @PathVariable Long id,
            @RequestBody Map<String, Boolean> body) {
        boolean enabled = Boolean.TRUE.equals(body.get("enabled"));
        return ResponseEntity.ok(authService.cambiarEstado(id, enabled));
    }
}
