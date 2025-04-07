package com.student.mgmt.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.student.mgmt.model.StudentDto;
import com.student.mgmt.service.StudentService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/students")
@Tag(name = "Student Management", description = "APIs for managing students")
public class StudentController {

    private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    @Operation(summary = "Get all students", description = "Retrieve a list of all students")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of students")
    @ApiResponse(responseCode = "404", description = "No students found")
    @CircuitBreaker(name = "studentCircuitBreaker", fallbackMethod = "fallbackCBGetAllStudents")
    @RateLimiter(name = "studentRateLimiter", fallbackMethod = "fallbackRLGetAllStudents")
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        logger.info("Fetching all students");
        return studentService.getAllStudents()
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get student by ID", description = "Retrieve a student by their ID")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved student")
    @ApiResponse(responseCode = "404", description = "Student not found")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        logger.info("Fetching student with ID: {}", id);
        return  studentService.getStudentById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/email")
    @Operation(summary = "Get student by email", description = "Retrieve a student by their email address")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved student")
    @ApiResponse(responseCode = "404", description = "Student not found")
    public ResponseEntity<StudentDto> getStudentByEmail(@RequestParam String email) {
        logger.info("Fetching student with email: {}", email);
        return studentService.getStudentByEmail(email)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping
    @Operation(summary = "Create a new student", description = "Add a new student to the system")
    @ApiResponse(responseCode = "201", description = "Successfully created student")
    public ResponseEntity<StudentDto> createStudent(@RequestBody @Valid StudentDto studentDto) {
        logger.info("Creating new student: {}", studentDto);
        return new ResponseEntity<>(studentService.createStudent(studentDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update student by ID", description = "Update an existing student by their ID")
    @ApiResponse(responseCode = "200", description = "Successfully updated student")
    @ApiResponse(responseCode = "404", description = "Student not found")
    public ResponseEntity<StudentDto> updateStudent(
        @PathVariable Long id, 
        @RequestBody @Valid StudentDto studentDto) {
        logger.info("Updating student with ID: {} with data: {}", id, studentDto);
        return studentService.updateStudent(id, studentDto)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PatchMapping("/{id}/email")
    @Operation(summary = "Update student email by ID", description = "Update the email of an existing student by their ID")
    @ApiResponse(responseCode = "200", description = "Successfully updated student email")
    @ApiResponse(responseCode = "404", description = "Student not found")
    public ResponseEntity<StudentDto> updateStudentByEmail(@PathVariable Long id, @RequestParam @Valid String email) {
        logger.info("Updating email for student with ID: {} to email: {}", id, email);
        return studentService.updateStudentByEmail(id, email)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete student by ID", description = "Delete a student from the system by their ID")
    @ApiResponse(responseCode = "200", description = "Successfully deleted student")
    @ApiResponse(responseCode = "404", description = "Student not found")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        logger.info("Deleting student with ID: {}", id);
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully");
    }

    public ResponseEntity<List<StudentDto>> fallbackCBGetAllStudents(Exception e) {
        logger.error("Error fetching all students: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
    
    public ResponseEntity<List<StudentDto>> fallbackRLGetAllStudents(Exception e) {
        logger.error("Rate limit exceeded while fetching all students: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(null);
    }
}
