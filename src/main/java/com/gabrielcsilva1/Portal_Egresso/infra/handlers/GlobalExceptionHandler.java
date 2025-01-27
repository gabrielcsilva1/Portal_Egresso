package com.gabrielcsilva1.Portal_Egresso.infra.handlers;

import java.util.HashMap;
import java.util.Map;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.GraduateAlreadyTakenTheCourseException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidCredentialsException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidEndYearException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.core.ConflictException;

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

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<Map<String, String>> handleBadRequestException(BadRequestException exception) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Bad request");
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<Map<String, String>> handleConflictException(ConflictException exception) {
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

  @ExceptionHandler(GraduateAlreadyTakenTheCourseException.class)
  public ResponseEntity<Map<String, String>> handleInvalidGraduateAlreadyTakenTheCourseException(GraduateAlreadyTakenTheCourseException exception) {
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

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<Map<String, String>> handleInvalidBadCredentialsException(BadCredentialsException exception) {
    Map<String, String> error = new HashMap<>();
    error.put("error", exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(InternalAuthenticationServiceException.class)
  public ResponseEntity<Map<String, String>> handleInternalAuthenticationServiceException(InternalAuthenticationServiceException exception) {
    Map<String, String> error = new HashMap<>();
    error.put("error", "Login/password incorrect");
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
