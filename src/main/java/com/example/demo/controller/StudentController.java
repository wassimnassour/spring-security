package com.example.demo.controller;



import com.example.demo.entity.Student;
import com.example.demo.exaptions.JwtAuthenticationException;
import com.example.demo.repo.StudentsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    StudentsRepo studentsRepo;
public  StudentController(StudentsRepo studentsRepo) {
    this.studentsRepo = studentsRepo;
}


    @GetMapping("/error")
    public void triggerError() {
        throw new JwtAuthenticationException("Test Exception: Unauthorized access 1232");
//        ResponseEntity.ok("error");
    }

    // GET /students - Retrieve all students
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