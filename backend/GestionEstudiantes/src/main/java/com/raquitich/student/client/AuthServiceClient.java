package com.raquitich.student.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Cliente HTTP para comunicarse con ServicioAutenticacion.
 * Cuando un directivo crea un estudiante, este cliente crea
 * las credenciales de acceso en ServicioAutenticacion.
 */
@Component
public class AuthServiceClient {

    private static final Logger log = LoggerFactory.getLogger(AuthServiceClient.class);

    @Value("${app.auth-service.url}")
    private String authServiceUrl;

    @Value("${app.auth-service.internal-api-key}")
    private String internalApiKey;

    private final RestTemplate restTemplate;

    public AuthServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Crea las credenciales del estudiante en ServicioAutenticacion.
     *
     * @param username  nombre de usuario
     * @param email     correo electrónico
     * @param password  contraseña en texto plano (ServicioAutenticacion la cifra)
     * @param nombre    nombre completo
     * @param role      rol a asignar (ej: ROLE_ESTUDIANTE)
     * @throws RuntimeException si ServicioAutenticacion rechaza la petición
     */
    public void crearCredenciales(String username, String email,
                                  String password, String nombre, String role) {
        String url = authServiceUrl + "/internal/users/create";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-Internal-API-Key", internalApiKey);

        Map<String, String> body = Map.of(
                "username", username,
                "email",    email,
                "password", password,
                "nombre",   nombre,
                "role",     role
        );

        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
            log.info("Credenciales creadas en ServicioAutenticacion para '{}'", username);
        } catch (HttpClientErrorException e) {
            log.error("Error al crear credenciales en ServicioAutenticacion: {} - {}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw new RuntimeException(
                    "No se pudieron crear las credenciales de acceso: " + e.getResponseBodyAsString()
            );
        }
    }
}
