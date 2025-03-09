package com.example.demo.repo;

import com.example.demo.entity.Users;
import lombok.Data;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface UserRepo extends JpaRepository<Users , Long> {
    Users findByUsername(String username);
}
