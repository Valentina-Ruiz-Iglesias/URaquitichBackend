package com.raquitich.auth.service;

import com.raquitich.auth.dto.AdminCreateUserRequest;
import com.raquitich.auth.dto.AuthResponse;
import com.raquitich.auth.dto.ChangeRoleRequest;
import com.raquitich.auth.dto.InternalCreateUserRequest;
import com.raquitich.auth.dto.LoginRequest;
import com.raquitich.auth.dto.RegisterRequest;
import com.raquitich.auth.dto.UserResponse;
import com.raquitich.auth.model.Role;
import com.raquitich.auth.model.RoleName;
import com.raquitich.auth.model.User;
import com.raquitich.auth.repository.RoleRepository;
import com.raquitich.auth.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;

    public AuthService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager,
                       CustomUserDetailsService customUserDetailsService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
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

    // ── Admin: gestión de usuarios ─────────────────────────────────────────

    public List<UserResponse> listarUsuarios() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional
    public UserResponse crearUsuarioAdmin(AdminCreateUserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("El username ya está en uso");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El email ya está en uso");
        }

        RoleName roleName = resolveRole(request.getRole());
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setNombre(request.getNombre() != null ? request.getNombre() : request.getUsername());
        user.setPassword(passwordEncoder.encode(
                request.getPassword() != null ? request.getPassword() : "Raquitich2024!"));
        user.setEnabled(true);
        user.setRoles(Set.of(role));

        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse cambiarRol(Long id, ChangeRoleRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        RoleName roleName = resolveRole(request.getRole());
        Role role = roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));

        user.setRoles(Set.of(role));
        return UserResponse.from(userRepository.save(user));
    }

    @Transactional
    public UserResponse cambiarEstado(Long id, boolean enabled) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        user.setEnabled(enabled);
        return UserResponse.from(userRepository.save(user));
    }

    private RoleName resolveRole(String roleStr) {
        if (roleStr == null || roleStr.isBlank()) return RoleName.ROLE_ESTUDIANTE;
        // Solo se permiten los 3 roles del sistema
        return switch (roleStr) {
            case "ROLE_DOCENTE"    -> RoleName.ROLE_DOCENTE;
            case "ROLE_DIRECTIVO"  -> RoleName.ROLE_DIRECTIVO;
            default                -> RoleName.ROLE_ESTUDIANTE;
        };
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByUsernameOrEmail(request.getUsername(), request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

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