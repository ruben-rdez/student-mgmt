package com.student.mgmt.model;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email address is not valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "Mobile phone is not valid")
    @NotBlank(message = "Mobile phone is required")
    private String mobile;

    @NotBlank(message = "Gender is required")
    private String gender;

    @Min(value = 10, message = "Age must be at least 10")
    @Max(value = 80, message = "Age must be at most 80")
    private int age;

    @NotBlank(message = "Nationality is required")
    private String nationality;

}
