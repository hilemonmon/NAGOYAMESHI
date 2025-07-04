package com.example.nagoyameshi.repository;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
