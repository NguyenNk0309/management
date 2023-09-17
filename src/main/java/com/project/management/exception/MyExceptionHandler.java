package com.project.management.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(MyException.class)
    public ResponseEntity<String> handleCustomException(MyException e) {
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}