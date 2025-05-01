package com.student.mgmt.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Student Management API",
                version = "1.0",
                description = "REST API documentation for Student Management System"
        )
)

public class SwaggerConfig {
}
