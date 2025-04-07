package com.student.mgmt.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.student.mgmt.entity.Student;
import com.student.mgmt.exception.EmailAlreadyExistsException;
import com.student.mgmt.exception.StudentNotFoundException;
import com.student.mgmt.model.StudentDto;
import com.student.mgmt.repository.StudentRepository;
import com.student.mgmt.util.StudentMapper;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Optional<List<StudentDto>> getAllStudents() {
        logger.info("Fetching all students");
        List<Student> students = studentRepository.findAll();
        if(students.isEmpty()) {
            logger.warn("No students found");
            throw new StudentNotFoundException("No students found");
        }
        return Optional.ofNullable(StudentMapper.convertToDTOList(students));
    }

    public Optional<StudentDto> getStudentById(Long id) {
        logger.info("Fetching student with ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        return Optional.ofNullable(StudentMapper.convertToDto(student));
    }

    public Optional<StudentDto> getStudentByEmail(String email) {
        logger.info("Fetching student with email: {}", email);
        Student student = studentRepository.findByEmail(email)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        return Optional.ofNullable(StudentMapper.convertToDto(student));
    }
    
    public StudentDto createStudent(StudentDto studentDto) {
        logger.info("Creating new student: {}", studentDto);
        Student student = StudentMapper.convertToEntitySave(studentDto);
        student = studentRepository.save(student);
        return StudentMapper.convertToDto(student);
    }

    public Optional<StudentDto> updateStudent(Long id, StudentDto studentDto) {
        logger.info("Updating student with ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        if(studentRepository.findByIdAndEmail(id, studentDto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Student with email " + studentDto.getEmail() + " already exists");
        }
 
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setMobile(studentDto.getMobile());
        student.setGender(studentDto.getGender());
        student.setAge(studentDto.getAge());
        student.setNationality(studentDto.getNationality());       
        return Optional.ofNullable(StudentMapper.convertToDto(studentRepository.save(student)));
    }

    public Optional<StudentDto> updateStudentByEmail(Long id, String email) {
        logger.info("Updating student with ID: {} and email: {}", id, email);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));
        
        if(studentRepository.findByEmail(email).isPresent()) {
            throw new EmailAlreadyExistsException("Student with email " + email + " already exists");
        }

        student.setEmail(email);
        return Optional.ofNullable(StudentMapper.convertToDto(studentRepository.save(student)));
    }

    public void deleteStudent(Long id) {
        logger.info("Deleting student with ID: {}", id);
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        if(student != null) {
            studentRepository.deleteById(id);
        }
    }
}
