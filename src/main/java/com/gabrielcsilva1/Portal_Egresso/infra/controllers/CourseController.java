package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.CourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.CreateCourseUseCase;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/course")
public class CourseController {
  @Autowired
  private CreateCourseUseCase createCourseUseCase;

  @PostMapping
  public ResponseEntity<Object> save(@Valid @RequestBody CourseDTO courseDTO) {
    this.createCourseUseCase.execute(courseDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
