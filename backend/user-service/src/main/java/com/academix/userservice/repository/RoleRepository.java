package com.academix.userservice.repository;

import com.academix.userservice.dao.Role;
import com.academix.userservice.dao.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleEnum name);
}
