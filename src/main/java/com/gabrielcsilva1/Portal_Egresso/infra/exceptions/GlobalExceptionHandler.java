package com.gabrielcsilva1.Portal_Egresso.infra.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions.CoordinatorNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException exception) {
    Map<String, String> errors = new HashMap<String, String>();

    exception.getBindingResult()
      .getAllErrors()
      .forEach(error -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
      });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler(CoordinatorNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleCoordinatorNotFoundException(CoordinatorNotFoundException exception) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Bad request");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(Exception exception) {
      Map<String, String> error = new HashMap<>();
      System.out.println(exception.getMessage());
      error.put("error", "An unexpected error occurred");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
