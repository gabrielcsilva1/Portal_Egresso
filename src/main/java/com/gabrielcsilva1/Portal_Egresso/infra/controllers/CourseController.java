package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.CourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.course.UpdateCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
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

  @GetMapping
  public ResponseEntity<Object> fetchCourses() {
    List<Course> listOfCourses = this.courseService.fetchCourses();
    // TODO: Create a presenter
    return ResponseEntity.ok(listOfCourses);
  }

  @PostMapping
  @RequestMapping("/egress")
  public ResponseEntity<Object> registerEgressInCourse(@Valid @RequestBody EgressCourseDTO egressCourseDTO) {
    this.courseService.registerEgressInCourse(egressCourseDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @DeleteMapping("/egress/{id}")
  public ResponseEntity<Object> unregisterEgressInCourse(@PathVariable UUID id){
    this.courseService.unregisterEgressInCourse(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Object> updateCourse(
    @PathVariable UUID id, 
    @Valid @RequestBody UpdateCourseDTO courseDTO) {
    Course course = this.courseService.updateCourse(id, courseDTO);

    return ResponseEntity.status(HttpStatus.OK).body(course);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Object> deleteCourse(@PathVariable UUID id) {
    this.courseService.deleteCourse(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
