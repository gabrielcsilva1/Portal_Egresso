package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

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
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.GraduateCourse;

@DataJpaTest
@ActiveProfiles("test")
public class GraduateCourseRepositoryTest {
  @Autowired
  private GraduateCourseRepository graduateCourseRepository;

  @Autowired 
  private GraduateRepository graduateRepository;

  @Autowired
  private CourseRepository courseRepository;
  
  @Autowired
  private CoordinatorRepository coordinatorRepository;

  private Course course;

  private Graduate graduate;

  @BeforeEach
  public void setUp() {
    Coordinator coordinator = Coordinator.builder()
      .login("johndoe")
      .password("123456")
      .build();

    this.coordinatorRepository.save(coordinator);  

    var course = Course.builder()
      .coordinator(coordinator)
      .name("John Doe")
      .level("Graduação")
      .build();

    this.course = this.courseRepository.save(course);

    Graduate graduate = Graduate.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .build();

    this.graduate = this.graduateRepository.save(graduate);
  }

  @Test
  @DisplayName("should be able to register a graduate in a course")
  public void register_graduate_in_course() {
    GraduateCourse graduateCourse = GraduateCourse.builder()
      .graduate(graduate)
      .course(course)
      .startYear(2000)
      .endYear(2020)
      .build();

    GraduateCourse result = this.graduateCourseRepository.save(graduateCourse);

    assertNotNull(result);
    assertNotNull(result.getId());
  }

  @Test
  @DisplayName("should be able to get a graduate_course by id")
  public void get_graduate_course_by_id(){
    GraduateCourse graduateCourse = GraduateCourse.builder()
      .graduate(graduate)
      .course(course)
      .startYear(2000)
      .endYear(2020)
      .build();

    GraduateCourse savedGraduateCourse = this.graduateCourseRepository.save(graduateCourse);

    Optional<GraduateCourse> result = this.graduateCourseRepository.findById(savedGraduateCourse.getId());

    assertTrue(result.isPresent());
  }
}
