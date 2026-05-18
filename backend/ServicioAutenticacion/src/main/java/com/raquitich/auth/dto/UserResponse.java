package com.raquitich.auth.dto;

import com.raquitich.auth.model.User;

public class UserResponse {

    private Long   id;
    private String username;
    private String email;
    private String nombre;
    private String role;
    private boolean enabled;

    public static UserResponse from(User u) {
        UserResponse r = new UserResponse();
        r.id       = u.getId();
        r.username = u.getUsername();
        r.email    = u.getEmail();
        r.nombre   = u.getNombre();
        r.enabled  = u.isEnabled();
        r.role     = u.getRoles().stream()
                       .findFirst()
                       .map(ro -> ro.getName().name())
                       .orElse("SIN_ROL");
        return r;
    }

    public Long    getId()       { return id; }
    public String  getUsername() { return username; }
    public String  getEmail()    { return email; }
    public String  getNombre()   { return nombre; }
    public String  getRole()     { return role; }
    public boolean isEnabled()   { return enabled; }
}
