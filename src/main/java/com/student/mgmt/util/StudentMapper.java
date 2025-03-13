package com.student.mgmt.util;

import java.util.List;

import com.student.mgmt.entity.Student;
import com.student.mgmt.model.StudentDto;

public class StudentMapper {

    public static StudentDto convertToDto(Student student) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(student.getId());
        studentDto.setName(student.getName());
        studentDto.setEmail(student.getEmail());
        studentDto.setMobile(student.getMobile());
        studentDto.setGender(student.getGender());
        studentDto.setAge(student.getAge());
        studentDto.setNationality(student.getNationality());
        return studentDto;
    }

    public static List<StudentDto> convertToDTOList(List<Student> students) {
        return students.stream()
                    .map(StudentMapper::convertToDto)
                    .toList();
    }

    public static Student convertToEntity(StudentDto studentDto) {
        Student student = new Student();
        student.setId(studentDto.getId());
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setMobile(studentDto.getMobile());
        student.setGender(studentDto.getGender());
        student.setAge(studentDto.getAge());
        student.setNationality(studentDto.getNationality());    
        return student;
    }

    public static List<Student> convertToEntityList(List<StudentDto> studentDtos) {
        return studentDtos.stream()
                    .map(StudentMapper::convertToEntity)
                    .toList();
    }

    public static Student convertToEntitySave(StudentDto studentDto) {
        Student student = new Student();
        student.setName(studentDto.getName());
        student.setEmail(studentDto.getEmail());
        student.setMobile(studentDto.getMobile());
        student.setGender(studentDto.getGender());
        student.setAge(studentDto.getAge());
        student.setNationality(studentDto.getNationality());    
        return student;
    }

}
