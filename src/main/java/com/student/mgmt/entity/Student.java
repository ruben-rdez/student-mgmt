package com.student.mgmt.entity;

import com.student.mgmt.listener.AuditListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditListener.class)
@Table(name = "students")
public class Student {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String mobile;
    private String gender;
    private int age;
    private String nationality;

    public Student(String name, String email, String mobile, 
        String gender, int age, String nationality) {
            this.name = name;
            this.email = email;
            this.mobile = mobile;
            this.gender = gender;
            this.age = age;
            this.nationality = nationality;
    }
}