package com.student.mgmt.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.student.mgmt.entity.Student;
import com.student.mgmt.exception.EmailAlreadyExistsException;
import com.student.mgmt.exception.StudentNotFoundException;
import com.student.mgmt.model.StudentDto;
import com.student.mgmt.repository.StudentRepository;
import com.student.mgmt.util.StudentMapper;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private StudentMapper studentMapper;

    @InjectMocks
    private StudentService studentService;

    private Student student;

    private StudentDto studentDto;

    @BeforeEach
    void setUp() {
        student = new Student(
            1L,
            "Suki",
            "suki@mail.com",
            "1234567890", 
            "Female",
            16,
            "Japanese");

        studentDto = new StudentDto(
            1L,
            "Suki",
            "suki@mail.com",
            "123456789",
            "Female",
            16,
            "Japanese");
    }

    @Test
    void testCreateStudent() {
        StudentDto inputStudentDto = new StudentDto();
        inputStudentDto.setName("Suki");
        inputStudentDto.setEmail("suki@mail.com");
        inputStudentDto.setMobile("123456789");
        inputStudentDto.setGender("Japanese");
        inputStudentDto.setAge(16);
        inputStudentDto.setNationality("Japanese");

        Student inputStudent = new Student();
        inputStudent.setName("Suki");
        inputStudent.setEmail("suki@mail.com");
        inputStudent.setMobile("123456789");
        inputStudent.setGender("Japanese");
        inputStudent.setAge(16);
        inputStudent.setNationality("Japanese");

        try(MockedStatic<StudentMapper> mapperMock = mockStatic(StudentMapper.class)){
            mapperMock.when(() -> StudentMapper.convertToEntitySave(inputStudentDto)).thenReturn(inputStudent);
            when(studentRepository.save(inputStudent)).thenReturn(student);
            mapperMock.when(() -> StudentMapper.convertToDto(student)).thenReturn(studentDto);  
        
            StudentDto result = studentService.createStudent(inputStudentDto);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals("Suki", result.getName());

            verify(studentRepository).save(inputStudent);
        }
        
    }

    @Test
    void testDeleteStudent_WhenStudentFound() {
        Long studentId = 1L;
        Student student = new Student();
        student.setId(studentId);
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));

        studentService.deleteStudent(studentId);

        verify(studentRepository).deleteById(studentId);
    }

    @Test
    void testDeleteStudent_WhenStudentNotFound() {
        Long studentId = 2L;
        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, 
            () -> studentService.deleteStudent(studentId));

        verify(studentRepository, never()).deleteById(anyLong());
    }   

    @Test
    void testGetAllStudents_WhenStudentsFound() {
        List<Student> students = List.of(student);
        List<StudentDto> studentsDto = new ArrayList<>(List.of(studentDto));
        when(studentRepository.findAll()).thenReturn(students);
        mockStatic(StudentMapper.class).when(
            () -> StudentMapper.convertToDTOList(students)).thenReturn(studentsDto);

        Optional<List<StudentDto>> result = studentService.getAllStudents();

        assert result.isPresent();
        assert result.get().size() == 1;
        assert result.get().get(0).getName().equals("Suki");
        assertEquals("suki@mail.com", result.get().get(0).getEmail());
        verify(studentRepository).findAll();
    }

    @Test
    void testGetAllStudents_WhenStudentsNotFound() {
        when(studentRepository.findAll()).thenReturn(Collections.emptyList());
        assertThrows(StudentNotFoundException.class, () -> studentService.getAllStudents());    
    }   

    @Test
    void testGetStudentByEmail_WhenStudentFound() {
        String email = "suki@mail.com";
        when(studentRepository.findByEmail(email)).thenReturn(Optional.of(student));
        try(MockedStatic<StudentMapper> mapperMock = mockStatic(StudentMapper.class)){
            mapperMock.when(() -> StudentMapper.convertToDto(student)).thenReturn(studentDto);

            Optional<StudentDto> result = studentService.getStudentByEmail(email);

            assertEquals(1L, result.get().getId());
            assertEquals("Suki", result.get().getName());
            assertEquals(email, result.get().getEmail());
            assertTrue(result.isPresent());

            verify(studentRepository).findByEmail(email);
        }
    }

    @Test
    void testGetStudentByEmail_WhenStudentNotFound() {
        String email = "notfound@mail.com";
        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, 
            () -> studentService.getStudentByEmail(email));

    }

    @Test
    void testGetStudentById_WhenStudentFound() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        try(MockedStatic<StudentMapper> mapperMock = mockStatic(StudentMapper.class)){
            mapperMock.when(() -> StudentMapper.convertToDto(student)).thenReturn(studentDto);

            Optional<StudentDto> result = studentService.getStudentById(1L);

            assertEquals(1L, result.get().getId());
            assertEquals("Suki", result.get().getName());
            assertTrue(result.isPresent());
    
            verify(studentRepository).findById(1L);
    
        }
    }

    @Test
    void testGetStudentById_WhenStudentNotFound() {
        Long studentId = 2L;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        assertThrows(StudentNotFoundException.class, 
            () -> studentService.getStudentById(studentId));
    }

    @Test
    void testUpdateStudent_WhenStudentFound() {
        Long studentId = 1L;
        StudentDto inputStudentDto = new StudentDto();
        inputStudentDto.setName("Suki");
        inputStudentDto.setEmail("suki@mail.com");
        inputStudentDto.setMobile("123456789");
        inputStudentDto.setGender("Japanese");
        inputStudentDto.setAge(16);
        inputStudentDto.setNationality("Japanese");

        Student inputStudent = new Student();
        inputStudent.setName("Suki");
        inputStudent.setEmail("suki@mail.com");
        inputStudent.setMobile("123456789");
        inputStudent.setGender("Japanese");
        inputStudent.setAge(16);
        inputStudent.setNationality("Japanese");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentRepository.findByIdAndEmail(studentId, inputStudentDto.getEmail())).thenReturn(Optional.empty());

        try(MockedStatic<StudentMapper> mapperMock = mockStatic(StudentMapper.class)){
            mapperMock.when(() -> StudentMapper.convertToEntitySave(inputStudentDto)).thenReturn(inputStudent);
            when(studentRepository.save(student)).thenReturn(student);
            mapperMock.when(() -> StudentMapper.convertToDto(student)).thenReturn(studentDto);  
        
            Optional<StudentDto> result = studentService.updateStudent(studentId, inputStudentDto);

            assertNotNull(result);
            assertEquals(1L, result.get().getId());
            assertEquals("Suki", result.get().getName());

            verify(studentRepository).save(student);
        }
    }

    @Test
    void testUpdateStudent_WhenStudentNotFound() {
        Long studentId = 2L;
        StudentDto inputStudentDto = new StudentDto();
        inputStudentDto.setName("Suki");
        inputStudentDto.setEmail("suki@mail.com");
        inputStudentDto.setMobile("123456789");
        inputStudentDto.setGender("Japanese");
        inputStudentDto.setAge(16);
        inputStudentDto.setNationality("Japanese");

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, 
            () -> studentService.updateStudent(studentId, inputStudentDto));

        verify(studentRepository, never()).save(student);
    }
    
    @Test
    void testUpdateStudent_WhenStudentWhenEmailExists() {
        Long studentId = 1L;
        StudentDto inputStudentDto = new StudentDto();
        inputStudentDto.setName("Suki");
        inputStudentDto.setEmail("suki@mail.com");
        inputStudentDto.setMobile("123456789");
        inputStudentDto.setGender("Japanese");
        inputStudentDto.setAge(16);
        inputStudentDto.setNationality("Japanese");

        Student inputStudent = new Student();
        inputStudent.setId(studentId);
        inputStudent.setName("Tutsi");
        inputStudent.setEmail("tutsi@mail.com");
        inputStudent.setMobile("123456789");
        inputStudent.setGender("Female");
        inputStudent.setAge(38);
        inputStudent.setNationality("French");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentRepository.findByIdAndEmail(studentId, inputStudentDto.getEmail()))
        .thenReturn(Optional.of(new Student()));

        assertThrows(EmailAlreadyExistsException.class, () -> 
            studentService.updateStudent(studentId, inputStudentDto));
            
        verify(studentRepository, never()).save(student);
    }    

    @Test
    void testUpdateStudentByEmail_WhenStudentFound() {
        Long studentId = 1L;
        String email = "sukis@mail.com";

        StudentDto savedStudentDto = new StudentDto();
        savedStudentDto.setId(studentId);
        savedStudentDto.setName("Suki");
        savedStudentDto.setEmail("sukis@mail.com");
        savedStudentDto.setMobile("123456789");
        savedStudentDto.setGender("Japanese");
        savedStudentDto.setAge(16);
        savedStudentDto.setNationality("Japanese");

        Student savedStudent = new Student();
        savedStudent.setId(studentId);
        savedStudent.setName("Suki");
        savedStudent.setEmail("sukis@mail.com");
        savedStudent.setMobile("123456789");
        savedStudent.setGender("Japanese");
        savedStudent.setAge(16);
        savedStudent.setNationality("Japanese");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(studentRepository.save(student)).thenReturn(savedStudent);
        try(MockedStatic<StudentMapper> mapperMock = mockStatic(StudentMapper.class)){
            mapperMock.when(() -> StudentMapper.convertToDto(savedStudent))
                .thenReturn(savedStudentDto);

            Optional<StudentDto> result = studentService.updateStudentByEmail(studentId, email);

            assertTrue(result.isPresent());
            assertEquals(studentId, result.get().getId());
            assertEquals(savedStudentDto.getEmail(), result.get().getEmail());
        }
   }

    @Test
    void testUpdateStudentByEmail_WhenStudentNotFound() {
        Long studentId = 2L;
        StudentDto inputStudentDto = new StudentDto();
        inputStudentDto.setName("Suki");
        inputStudentDto.setEmail("suki@mail.com");
        inputStudentDto.setMobile("123456789");
        inputStudentDto.setGender("Japanese");
        inputStudentDto.setAge(16);
        inputStudentDto.setNationality("Japanese");

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, 
            () -> studentService.updateStudent(studentId, inputStudentDto));

        verify(studentRepository, never()).save(student);
    }

    @Test
    void testUpdateStudentByEmail_WhenEmailExists() {
        Long studentId = 1L;
        StudentDto inputStudentDto = new StudentDto();
        inputStudentDto.setName("Suki");
        inputStudentDto.setEmail("suki@mail.com");
        inputStudentDto.setMobile("123456789");
        inputStudentDto.setGender("Japanese");
        inputStudentDto.setAge(16);
        inputStudentDto.setNationality("Japanese");

        Student inputStudent = new Student();
        inputStudent.setId(studentId);
        inputStudent.setName("Tutsi");
        inputStudent.setEmail("tutsi@mail.com");
        inputStudent.setMobile("123456789");
        inputStudent.setGender("Female");
        inputStudent.setAge(38);
        inputStudent.setNationality("French");

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentRepository.findByIdAndEmail(studentId, inputStudentDto.getEmail()))
        .thenReturn(Optional.of(new Student()));

        assertThrows(EmailAlreadyExistsException.class, () -> 
            studentService.updateStudent(studentId, inputStudentDto));
            
        verify(studentRepository, never()).save(student);
    }

}
