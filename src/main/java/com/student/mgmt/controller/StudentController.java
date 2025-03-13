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

import jakarta.validation.Valid;

@RestController
@RequestMapping("/students")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        return ResponseEntity.ok(studentService.getAllStudents());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Long id) {
        return  ResponseEntity.ok(studentService.getStudentById(id)); 
    }

    @GetMapping("/email")
    public ResponseEntity<StudentDto> getStudentByEmail(@RequestParam String email) {
        return ResponseEntity.ok(studentService.getStudentByEmail(email));
    }

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody @Valid StudentDto studentDto) {
        return new ResponseEntity<>(studentService.createStudent(studentDto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(
        @PathVariable Long id, 
        @RequestBody @Valid StudentDto studentDto) {
        return new ResponseEntity<>(studentService.updateStudent(id, studentDto), HttpStatus.OK);
    }

    @PatchMapping("/{id}/email")
    public ResponseEntity<StudentDto> updateStudentByEmail(@PathVariable Long id, @RequestParam @Valid String email) {
        return new ResponseEntity<>(studentService.updateStudentByEmail(id,email), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok("Student deleted successfully");
    }

}
