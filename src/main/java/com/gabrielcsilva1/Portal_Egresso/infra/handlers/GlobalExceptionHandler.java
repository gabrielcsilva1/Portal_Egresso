package com.gabrielcsilva1.Portal_Egresso.infra.handlers;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.CoordinatorNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.CourseNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressAlreadyExistsException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressAlreadyTakenTheCourseException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidCredentialsException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidEndYearException;

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

  @ExceptionHandler(CourseNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleCourseNotFoundException(CourseNotFoundException exception) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Bad request");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(EgressNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleEgressNotFoundException(EgressNotFoundException exception) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Bad request");
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(EgressAlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleEgressAlreadyExistsException(EgressAlreadyExistsException exception) {
    Map<String, String> error = new HashMap<>();
    error.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  @ExceptionHandler(InvalidEndYearException.class)
  public ResponseEntity<Map<String, String>> handleInvalidEndYearException(InvalidEndYearException exception) {
    Map<String, String> error = new HashMap<>();
    error.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(EgressAlreadyTakenTheCourseException.class)
  public ResponseEntity<Map<String, String>> handleInvalidEgressAlreadyTakenTheCourseException(EgressAlreadyTakenTheCourseException exception) {
    Map<String, String> error = new HashMap<>();
    error.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(InvalidCredentialsException.class)
  public ResponseEntity<Map<String, String>> handleInvalidInvalidCredentialsException(InvalidCredentialsException exception) {
    Map<String, String> error = new HashMap<>();
    error.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, String>> handleGenericException(Exception exception) {
      Map<String, String> error = new HashMap<>();
      System.out.println(exception.getMessage());
      error.put("error", "An unexpected error occurred");
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }
}
