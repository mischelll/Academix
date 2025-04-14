package com.academix.user.dao.repository;

import com.academix.user.dao.Role;
import com.academix.user.dao.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(RoleEnum name);
}
