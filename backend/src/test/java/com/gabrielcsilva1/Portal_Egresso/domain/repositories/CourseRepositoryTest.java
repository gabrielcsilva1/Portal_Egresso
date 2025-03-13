package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;

@DataJpaTest
@ActiveProfiles("test")
public class CourseRepositoryTest {
  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private CoordinatorRepository coordinatorRepository;

  private Coordinator coordinator;

  @BeforeEach
  public void registerCoordinator() {
    Coordinator coordinator = Coordinator.builder()
      .login("johndoe")
      .password("123456")
      .build();

    this.coordinator = this.coordinatorRepository.save(coordinator);  
  }

  @Test
  @DisplayName("should be able to create a new course")
  public void save_new_course() {
    var course = Course.builder()
      .coordinator(coordinator)
      .name("John Doe")
      .level("Graduação")
      .build();

    var result = this.courseRepository.save(course);

    assertNotNull(result);
    assertNotNull(result.getId());
  }

  @Test
  @DisplayName("should be able to find a course by id")
  public void find_course_by_id() {
    var course = Course.builder()
      .coordinator(coordinator)
      .name("John Doe")
      .level("Graduação")
      .build();

    Course savedCourse = this.courseRepository.save(course);

    Optional<Course> result = this.courseRepository.findById(savedCourse.getId());

    assertTrue(result.isPresent());
    assertEquals(savedCourse.getId(), result.get().getId());
  }
}
