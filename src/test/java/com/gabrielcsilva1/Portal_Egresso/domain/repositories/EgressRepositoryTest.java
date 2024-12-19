package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.EgressCourse;
import com.gabrielcsilva1.Portal_Egresso.infra.queryfilters.EgressQueryFilter;

@DataJpaTest
@ActiveProfiles("test")
public class EgressRepositoryTest {
  @Autowired
  private EgressRepository egressRepository;

  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private EgressCourseRepository egressCourseRepository;

  @Test
  @DisplayName("should be able to create a new egress")
  public void save_new_egress() {
    Egress egress = Egress.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .build();

    Egress result = this.egressRepository.save(egress);

    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals(result.getName(), egress.getName());
  }

  @Test
  @DisplayName("should be able to get a egress by id")
  public void get_egress_by_id() {
    Egress egress = Egress.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .build();

    Egress savedEgress = this.egressRepository.save(egress);

    Optional<Egress> result = this.egressRepository.findById(savedEgress.getId());

    assertTrue(result.isPresent());
  }

  @Test
  @DisplayName("should be able to fetch user with filter (query)")
  public void fetch_egresses_with_query_filter_success() {
    Egress egress = Egress.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .build();

    egress = egressRepository.save(egress);

    var queryFilter = EgressQueryFilter.builder()
      .query(egress.getName())
      .build();

    var result = egressRepository.findAll(queryFilter.toSpecification());

    assertThat(result).hasSize(1);
  }

  @Test
  @DisplayName("should be able to fetch user with filters (query, year and courseName)")
  public void fetch_egresses_with_filters_success() {
    Egress egress = Egress.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .build();

    egress = egressRepository.save(egress);

    Coordinator coordinator = Coordinator.builder()
      .login("johndoe")
      .password("123456")
      .build();
    
    coordinator = coordinatorRepository.save(coordinator);

    Course course = Course.builder()
      .coordinator(coordinator)
      .name("Informática")
      .level("Graduação")
      .build();

    course = courseRepository.save(course);

    EgressCourse egressCourse = EgressCourse.builder()
      .course(course)
      .egress(egress)
      .startYear(2000)
      .build();

    egressCourseRepository.save(egressCourse);

    var queryFilter = EgressQueryFilter.builder()
      .courseName(course.getName())
      .query(egress.getName())
      .year(egressCourse.getStartYear())
      .build();

    var result = egressRepository.findAll(queryFilter.toSpecification());

    assertThat(result).hasSize(1);
  }
}
