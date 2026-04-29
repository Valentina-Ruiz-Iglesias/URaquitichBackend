package com.raquitich.auth.repository;

import com.raquitich.auth.model.Role;
import com.raquitich.auth.model.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}