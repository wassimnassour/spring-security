package com.example.demo.controller;


import com.example.demo.entity.Student;
import com.example.demo.repo.StudentsRepo;
import com.example.demo.specification.StudentSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/students")
public class StudentController {

    StudentsRepo studentsRepo;


    public StudentController(StudentsRepo studentsRepo, StudentSpecification studentSpecification) {
        this.studentsRepo = studentsRepo;
    }


    @GetMapping("/get")
    @PreAuthorize("hasRole('ROLE_STUDENT')")  // Matches "ROLE_STUDENT"
    public ResponseEntity<String> getResource() {
        return ResponseEntity.ok("GET request successful");
    }

    @GetMapping("/creator")
    @PreAuthorize("hasRole('CREATOR')")
    public ResponseEntity<String> getAdminResource() {
        return ResponseEntity.ok("This is an admin-only resource.");
    }

    @GetMapping
    public ResponseEntity<List<Student>> getStudents(
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String prefix,
            @RequestParam(required = false) String endWith) {

        Specification<Student> specification = Specification.where(null);

        // Apply age filter if provided
        if (age != null) {
            specification = specification.and(StudentSpecification.hasAgeGreaterThanTen(age));
        }

        // Apply prefix and endWith filters if provided
        if (prefix != null && endWith != null) {
            specification = specification.and(StudentSpecification.startWithPrefixAndEndWithKey(prefix, endWith));
        } else if (prefix != null) {
            specification = specification.and(StudentSpecification.startWithPrefix(prefix));
        } else if (endWith != null) {
            specification = specification.and(StudentSpecification.endWithKey(endWith));
        }

        return ResponseEntity.ok(studentsRepo.findAll(specification));
    }

    @GetMapping
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentsRepo.findAll());
    }

    // POST /students - Add a new student
    @PostMapping
    public String addStudent(@RequestBody Student student) {
        studentsRepo.save(student);
        return "Student added successfully!";
    }

}