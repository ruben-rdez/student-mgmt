package com.student.mgmt.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Value("${cors.allowed-origins}")
    private List<String> allowedOrigins;

    @Value("${cors.allowed-methods}")
    private List<String> allowedMethods;

    @Value("${cors.allowed-headers}")
    private List<String> allowedHeaders;

    @Value("${cors.allow-credentials}")
    private boolean allowCredentials;

/*  @Override
    public void addCorsMappings(CorsRegistry registry) {
        System.out.println("allowedMethods: " + allowedMethods);
        registry.addMapping("/students/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods(allowedMethods);
    }
*/
    @Bean
    public WebMvcConfigurer corsConfigurer() {  
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/students/**")
                    .allowedOrigins(allowedOrigins.toArray(new String[0]))
                    .allowedMethods(allowedMethods.toArray(new String[0]))
                    .allowedHeaders(allowedHeaders.toArray(new String[0]))
                    .allowCredentials(allowCredentials);
            }
        };
    }

}