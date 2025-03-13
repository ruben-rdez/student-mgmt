package com.student.mgmt.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.student.mgmt.entity.Student;
import com.student.mgmt.exception.EmailAlreadyExistsException;
import com.student.mgmt.exception.StudentNotFoundException;
import com.student.mgmt.model.StudentDto;
import com.student.mgmt.repository.StudentRepository;
import com.student.mgmt.util.StudentMapper;

@Service
public class StudentService {

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentDto> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return StudentMapper.convertToDTOList(students);
    }

    public StudentDto getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        return StudentMapper.convertToDto(student);
    }

    public StudentDto getStudentByEmail(String email) {
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        return StudentMapper.convertToDto(student);
    }
    
    public StudentDto createStudent(StudentDto studentDto) {
        Student student = StudentMapper.convertToEntitySave(studentDto);
        student = studentRepository.save(student);
        return StudentMapper.convertToDto(student);
    }

    public StudentDto updateStudent(Long id, StudentDto studentDto) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        if(studentRepository.findByEmail(studentDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Student with email " + studentDto.getEmail() + " already exists");
        }
 
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setMobile(studentDto.getMobile());
        student.setGender(studentDto.getGender());
        student.setAge(studentDto.getAge());
        student.setNationality(studentDto.getNationality());       
        return StudentMapper.convertToDto(studentRepository.save(student));
    }

    public StudentDto updateStudentByEmail(Long id, String email) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        
        if(studentRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("Student with email " + email + " already exists");
        }

        student.setEmail(email);
        return StudentMapper.convertToDto(studentRepository.save(student));
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }
 
}
