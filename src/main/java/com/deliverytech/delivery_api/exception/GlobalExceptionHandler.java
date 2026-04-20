package com.deliverytech.delivery_api.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public record GlobalExceptionHandler() {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> HandleBusiness(BusinessException exception) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            exception.getMessage(),
            System.currentTimeMillis(),
            null
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> HandleNotFound(EntityNotFoundException exception) {
        ErrorResponse error = new ErrorResponse(
            HttpStatus.NOT_FOUND.value(),
            exception.getMessage(),
            System.currentTimeMillis(),
            null
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> HandleValidation(MethodArgumentNotValidException exception) {
        List<ErrorResponse.ValidationError> validationErrors = 
        exception.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(e -> new ErrorResponse.ValidationError(e.getField(), e.getDefaultMessage()))
        .toList();
        
        ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation error",
            System.currentTimeMillis(),
            validationErrors
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
