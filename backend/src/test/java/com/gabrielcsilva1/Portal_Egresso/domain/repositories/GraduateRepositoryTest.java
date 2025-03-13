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
import org.springframework.transaction.annotation.Transactional;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.GraduateCourse;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;
import com.gabrielcsilva1.Portal_Egresso.dtos.queryParams.QueryGraduate;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
public class GraduateRepositoryTest {
  @Autowired
  private GraduateRepository graduateRepository;

  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private GraduateCourseRepository graduateCourseRepository;

  @Test
  @DisplayName("should be able to create a new graduate")
  public void save_new_graduate() {
    Graduate graduate = Graduate.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .password("123456")
      .build();

    Graduate result = this.graduateRepository.save(graduate);

    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals(result.getName(), graduate.getName());
  }

  @Test
  @DisplayName("should be able to get a graduate by id")
  public void get_graduate_by_id() {
    Graduate graduate = Graduate.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .password("123456")
      .build();

    Graduate savedGraduate = this.graduateRepository.save(graduate);

    Optional<Graduate> result = this.graduateRepository.findById(savedGraduate.getId());

    assertTrue(result.isPresent());
  }

  @Test
  @DisplayName("should be able to fetch user with filter (query)")
  public void fetch_graduates_with_query_filter_success() {
    Graduate graduate = Graduate.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .password("123456")
      .registrationStatus(StatusEnum.ACCEPTED)
      .build();

    graduate = graduateRepository.save(graduate);

    var queryFilter = QueryGraduate.builder()
      .query(graduate.getName())
      .build();

    var result = graduateRepository.findAll(queryFilter.toSpecification());

    assertThat(result).hasSize(1);
  }

  @Test
  @DisplayName("should be able to fetch user with filters (query, year and courseName)")
  public void fetch_graduates_with_filters_success() {
    Graduate graduate = Graduate.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .password("123456")
      .registrationStatus(StatusEnum.ACCEPTED)
      .build();

    graduate = graduateRepository.save(graduate);

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

    GraduateCourse graduateCourse = GraduateCourse.builder()
      .course(course)
      .graduate(graduate)
      .startYear(2000)
      .build();

    graduateCourseRepository.save(graduateCourse);

    var queryFilter = QueryGraduate.builder()
      .courseName(course.getName())
      .query(graduate.getName())
      .year(graduateCourse.getStartYear())
      .build();

    var result = graduateRepository.findAll(queryFilter.toSpecification());

    assertThat(result).hasSize(1);
  }
}
