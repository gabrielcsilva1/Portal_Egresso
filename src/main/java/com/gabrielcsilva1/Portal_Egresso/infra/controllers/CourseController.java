package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.CourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.services.CourseService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/course")
public class CourseController {
  @Autowired
  private CourseService courseService;

  @PostMapping
  public ResponseEntity<Object> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
    this.courseService.createCourse(courseDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @PostMapping
  @RequestMapping("/egress")
  public ResponseEntity<Object> registerEgressInCourse(@Valid @RequestBody EgressCourseDTO egressCourseDTO) {
    this.courseService.registerEgressInCourse(egressCourseDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }
}
