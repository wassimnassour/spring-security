package com.example.demo.repo;


import com.example.demo.entity.Role;
import com.example.demo.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo  extends JpaRepository<Role, Long> {
    Role findByName(RoleEnum name);
}
