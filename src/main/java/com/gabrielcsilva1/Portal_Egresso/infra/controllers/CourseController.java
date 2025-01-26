package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.CourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.GraduateCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.course.CourseResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.course.UpdateCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.services.CourseService;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/course")
@Tag(name = "Course")
public class CourseController {
  @Autowired
  private CourseService courseService;

  @PostMapping
  @ApiResponse(responseCode = "201", description = "Course created")
  public ResponseEntity<CourseResponse> createCourse(@Valid @RequestBody CourseDTO courseDTO) {
    var authentication = SecurityContextHolder.getContext().getAuthentication();
    Coordinator coordinator = (Coordinator) authentication.getPrincipal();

    Course course = this.courseService.createCourse(courseDTO, coordinator.getId());
    
    return ResponseEntity.status(HttpStatus.CREATED).body(
      CourseResponse.toResponse(course)
    );
  }

  @PutMapping("/{id}")
  @ApiResponse(responseCode = "204", description = "Course update")
  public ResponseEntity<Void> updateCourse(
    @PathVariable UUID id, 
    @Valid @RequestBody UpdateCourseDTO courseDTO) {
    this.courseService.updateCourse(id, courseDTO);

    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @GetMapping 
  @ApiResponse(responseCode = "200", description = "List of Courses")
  public ResponseEntity<List<CourseResponse>> fetchCourses() {
    List<Course> listOfCourses = this.courseService.fetchCourses();

    List<CourseResponse> coursePresenterList = listOfCourses.stream()
      .map(CourseResponse::toResponse)
      .toList();
    
    return ResponseEntity.ok(coursePresenterList);
  }

  @PostMapping("/graduate")
  @ApiResponse(responseCode = "201", description = "Graduate registered in Course")
  public ResponseEntity<Void> registerGraduateInCourse(@Valid @RequestBody GraduateCourseDTO graduateCourseDTO) {
    this.courseService.registerGraduateInCourse(graduateCourseDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(null);
  }

  @DeleteMapping("/graduate/{id}")
  @ApiResponse(responseCode = "204", description = "Graduate unregistered in Course")
  public ResponseEntity<Void> unregisterGraduateInCourse(@PathVariable UUID id){
    this.courseService.unregisterGraduateInCourse(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }

  @DeleteMapping("/{id}")
  @ApiResponse(responseCode = "204", description = "Graduate unregistered in Course")
  public ResponseEntity<Void> deleteCourse(@PathVariable UUID id) {
    this.courseService.deleteCourse(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
  }
}
