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
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.EgressCourse;

@DataJpaTest
@ActiveProfiles("test")
public class EgressCourseRepositoryTest {
  @Autowired
  private EgressCourseRepository egressCourseRepository;

  @Autowired 
  private EgressRepository egressRepository;

  @Autowired
  private CourseRepository courseRepository;
  
  @Autowired
  private CoordinatorRepository coordinatorRepository;

  private Course course;

  private Egress egress;

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

    Egress egress = Egress.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .build();

    this.egress = this.egressRepository.save(egress);
  }

  @Test
  @DisplayName("should be able to register a egress in a course")
  public void register_egress_in_course() {
    EgressCourse egressCourse = EgressCourse.builder()
      .egress(egress)
      .course(course)
      .startYear(2000)
      .endYear(2020)
      .build();

    EgressCourse result = this.egressCourseRepository.save(egressCourse);

    assertNotNull(result);
    assertNotNull(result.getId());
  }

  @Test
  @DisplayName("should be able to get a egress_course by id")
  public void get_egress_course_by_id(){
    EgressCourse egressCourse = EgressCourse.builder()
      .egress(egress)
      .course(course)
      .startYear(2000)
      .endYear(2020)
      .build();

    EgressCourse savedEgressCourse = this.egressCourseRepository.save(egressCourse);

    Optional<EgressCourse> result = this.egressCourseRepository.findById(savedEgressCourse.getId());

    assertTrue(result.isPresent());
  }
}
