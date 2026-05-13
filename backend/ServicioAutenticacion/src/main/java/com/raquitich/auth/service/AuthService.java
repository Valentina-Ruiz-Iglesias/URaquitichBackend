package com.raquitich.auth.service;

import com.raquitich.auth.dto.AuthResponse;
import com.raquitich.auth.dto.InternalCreateUserRequest;
import com.raquitich.auth.dto.LoginRequest;
import com.raquitich.auth.dto.RegisterRequest;
import com.raquitich.auth.model.Role;
import com.raquitich.auth.model.RoleName;
import com.raquitich.auth.model.User;
import com.raquitich.auth.repository.RoleRepository;
import com.raquitich.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        // Todo usuario nuevo siempre recibe ROLE_ESTUDIANTE
        // La asignación de otros roles es exclusiva de endpoints administrativos protegidos
        final RoleName roleName = RoleName.ROLE_ESTUDIANTE;

        Role userRole = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setNombre(request.getNombre());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setRoles(Set.of(userRole));

        userRepository.save(user);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);

        return new AuthResponse(token, user.getUsername(), user.getNombre(), userRole.getName().name());
    }

    /**
     * Crea un usuario desde un microservicio interno (ej: GestionEstudiantes).
     * NO devuelve token: el usuario deberá hacer login luego desde el frontend.
     */
    public void createInternalUser(InternalCreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }

        RoleName roleName = RoleName.ROLE_ESTUDIANTE;
        if (request.getRole() != null && !request.getRole().isBlank()) {
            try {
                roleName = RoleName.valueOf(request.getRole());
            } catch (IllegalArgumentException ignored) {
                // rol desconocido → se asigna ROLE_ESTUDIANTE por defecto
            }
        }

        final RoleName finalRoleName = roleName;   // final para usarla en la lambda
        Role userRole = roleRepository.findByName(finalRoleName)
                .orElseGet(() -> roleRepository.save(new Role(finalRoleName)));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setNombre(request.getNombre());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEnabled(true);
        user.setRoles(Set.of(userRole));

        userRepository.save(user);
    }

    public AuthResponse login(LoginRequest request) {
        // Busca por username o por correo electrónico
        User user = userRepository.findByUsername(request.getUsername())
                .orElseGet(() -> userRepository.findByEmail(request.getUsername())
                        .orElseThrow(() -> new RuntimeException("Credenciales inválidas")));

        // Verifica la contraseña directamente — sin pasar por Spring Security
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        if (!user.isEnabled()) {
            throw new RuntimeException("La cuenta está desactivada");
        }

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getUsername());
        String token = jwtService.generateToken(userDetails);

        String role = user.getRoles()
                .stream()
                .findFirst()
                .map(r -> r.getName().name())
                .orElse("ROLE_ESTUDIANTE");

        return new AuthResponse(token, user.getUsername(), user.getNombre(), role);
    }
}