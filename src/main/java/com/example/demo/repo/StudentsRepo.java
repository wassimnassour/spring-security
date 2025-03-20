package com.example.demo.repo;

import com.example.demo.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface StudentsRepo extends JpaRepository<Student, Long >, JpaSpecificationExecutor<Student> {
}


