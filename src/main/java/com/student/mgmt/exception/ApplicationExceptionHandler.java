package com.student.mgmt.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleInvalidArgumentException(
            MethodArgumentNotValidException methodArgumentNotValidException){
        Map<String, String> errorHandlerMap = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getFieldErrors().forEach(error -> {
            errorHandlerMap.put(error.getField(), error.getDefaultMessage());
        });
        return errorHandlerMap;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(StudentNotFoundException.class)
    public Map<String, String> handleStudentNotFoundException(
            StudentNotFoundException studentNotFoundException){
        Map<String, String> errorHandlerMap = new HashMap<>();
        errorHandlerMap.put("errorMessage", studentNotFoundException.getMessage());
        return errorHandlerMap;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public Map<String, String> handleEmailAlreadyExistsException(
        EmailAlreadyExistsException emailAlreadyExistsException){
        Map<String, String> errorHandlerMap = new HashMap<>();
        errorHandlerMap.put("errorMessage", emailAlreadyExistsException.getMessage());
        return errorHandlerMap;
    }

}
