package com.project.management.exception;

import com.project.management.dtos.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(MyException.class)
    public ResponseEntity<ResponseDTO> handleCustomException(MyException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(ResponseDTO.builder()
                        .status(e.getHttpStatus().value())
                        .data(null)
                        .message(e.getMessage())
                        .build());
    }
}