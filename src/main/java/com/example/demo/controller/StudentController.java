package com.example.demo.controller;



import com.example.demo.entity.Student;
import com.example.demo.repo.StudentsRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/students")
public class StudentController {

    StudentsRepo studentsRepo;
public  StudentController(StudentsRepo studentsRepo) {
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



    // GET /students - Retrieve all students
//    @GetMapping
//    public ResponseEntity<List<Student>> getAllStudents() {
//        return ResponseEntity.ok(studentsRepo.findAll());
//    }

    // POST /students - Add a new student
    @PostMapping
    public String addStudent(@RequestBody Student student) {
        studentsRepo.save(student);
        return "Student added successfully!";
    }

}