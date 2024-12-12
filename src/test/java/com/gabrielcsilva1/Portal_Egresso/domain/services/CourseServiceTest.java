package com.gabrielcsilva1.Portal_Egresso.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.CourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.EgressCourse;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressCourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.CoordinatorNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.CourseNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressAlreadyTakenTheCourseException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.InvalidEndYearException;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
  @InjectMocks
  private CourseService sut;

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private CoordinatorRepository coordinatorRepository;

  @Mock
  private EgressRepository egressRepository;

  @Mock
  private EgressCourseRepository egressCourseRepository;

  @Test
  @DisplayName("should be able to create a new course")
  public void create_course_success() {
    // DTO
    var courseDTO = CourseDTO.builder()
      .coordinatorId(UUID.randomUUID())
      .name("Course Name")
      .level("Graduation")
      .build();

    // Mocks
    var coordinatorMock = Coordinator.builder()
      .id(courseDTO.getCoordinatorId())
      .build();
    
    var courseMock = Course.builder()
      .coordinator(coordinatorMock)
      .name(courseDTO.getName())
      .level(courseDTO.getLevel())
      .build();

    when(this.coordinatorRepository.findById(coordinatorMock.getId()))
      .thenReturn(Optional.of(coordinatorMock));

    when(this.courseRepository.save(any(Course.class)))
      .thenReturn(courseMock);

    // Test
    Course result = this.sut.createCourse(courseDTO);

    assertEquals(result.getId(), courseMock.getId());
    assertEquals(result.getCoordinator().getId(), courseDTO.getCoordinatorId());
  }

  @Test
  @DisplayName("should not be able to create a new course with a coordinator identifier not valid")
  public void create_course_coordinator_not_found_exception() {
     // DTO
     var courseDTO = CourseDTO.builder()
     .coordinatorId(UUID.randomUUID())
     .name("Course Name")
     .level("Graduation")
     .build();

   // Mocks
   when(this.coordinatorRepository.findById(courseDTO.getCoordinatorId()))
     .thenReturn(Optional.empty());

   // Test
   assertThrows(CoordinatorNotFoundException.class, () -> {
      this.sut.createCourse(courseDTO);
   });
  }

  @Test
  @DisplayName("should be able to register a egress in a course")
  public void register_egress_in_course_success() {
    // DTO
    var egressCourseDTO = EgressCourseDTO.builder()
      .egressId(UUID.randomUUID())
      .courseId(UUID.randomUUID())
      .startYear(Integer.valueOf(2000))
      .endYear(Integer.valueOf(2020))
      .build();

    // Mocks
    Course courseMock = Course.builder()
      .id(egressCourseDTO.getCourseId())
      .build();

    Egress egressMock = Egress.builder()
      .id(egressCourseDTO.getEgressId())
      .build();

    EgressCourse egressCourseMock = EgressCourse.builder()
      .id(UUID.randomUUID())
      .egress(egressMock)
      .course(courseMock)
      .startYear(egressCourseDTO.getStartYear())
      .endYear(egressCourseDTO.getEndYear())
      .build();

    // Mock behavior
    when(courseRepository.findById(egressCourseDTO.getCourseId()))
      .thenReturn(Optional.of(courseMock));
    when(egressRepository.findById(egressCourseDTO.getEgressId()))
      .thenReturn(Optional.of(egressMock));
    when(egressCourseRepository.save(any(EgressCourse.class)))
      .thenReturn(egressCourseMock);

    // Test
    EgressCourse result = sut.registerEgressInCourse(egressCourseDTO);

    assertEquals(result.getId(), egressCourseMock.getId());
    assertEquals(result.getCourse().getId(), egressCourseDTO.getCourseId());
    assertEquals(result.getEgress().getId(), egressCourseDTO.getEgressId());
  }

  @Test
  @DisplayName("should not be able to register a invalid egress in a course")
  public void register_egress_in_course_egress_not_found() {
    // DTO
    var egressCourseDTO = EgressCourseDTO.builder()
      .egressId(UUID.randomUUID())
      .courseId(UUID.randomUUID())
      .startYear(Integer.valueOf(2000))
      .endYear(Integer.valueOf(2020))
      .build();

    // Mock behavior
    when(egressRepository.findById(egressCourseDTO.getEgressId()))
      .thenReturn(Optional.empty());

    // Test
    assertThrows(EgressNotFoundException.class, () -> {
      sut.registerEgressInCourse(egressCourseDTO) ;
    });
  }

  @Test
  @DisplayName("should not be able to register a egress in a invalid course")
  public void register_egress_in_course_course_not_found() {
    // DTO
    var egressCourseDTO = EgressCourseDTO.builder()
      .egressId(UUID.randomUUID())
      .courseId(UUID.randomUUID())
      .startYear(Integer.valueOf(2000))
      .endYear(Integer.valueOf(2020))
      .build();

    // Mocks
    Egress egressMock = Egress.builder()
      .id(egressCourseDTO.getEgressId())
      .build();

    // Mock behavior
    when(egressRepository.findById(egressCourseDTO.getEgressId()))
      .thenReturn(Optional.of(egressMock));
    when(courseRepository.findById(egressCourseDTO.getCourseId()))
      .thenReturn(Optional.empty());

    // Test
    assertThrows(CourseNotFoundException.class, () -> {
      sut.registerEgressInCourse(egressCourseDTO);
    });
  }

  @Test
  @DisplayName("should not be able to register an egress in the course with an end year before the start year")
  public void register_egress_in_course_invalid_end_year() {
    // DTO
    var egressCourseDTO = EgressCourseDTO.builder()
      .egressId(UUID.randomUUID())
      .courseId(UUID.randomUUID())
      .startYear(Integer.valueOf(2000))
      .endYear(Integer.valueOf(1990))
      .build();

    // Mocks
    Course courseMock = Course.builder()
      .id(egressCourseDTO.getCourseId())
      .build();

    Egress egressMock = Egress.builder()
      .id(egressCourseDTO.getEgressId())
      .build();

    // Mock behavior
    when(courseRepository.findById(egressCourseDTO.getCourseId()))
      .thenReturn(Optional.of(courseMock));
    when(egressRepository.findById(egressCourseDTO.getEgressId()))
      .thenReturn(Optional.of(egressMock));

    // Test
    assertThrows(InvalidEndYearException.class, () -> {
      sut.registerEgressInCourse(egressCourseDTO);
    });
  }

  @Test
  @DisplayName("should not be able to register an egress twice in the same course")
  public void register_egress_in_course_egress_already_taken_the_course() {
    // DTO
    var egressCourseDTO = EgressCourseDTO.builder()
      .egressId(UUID.randomUUID())
      .courseId(UUID.randomUUID())
      .startYear(Integer.valueOf(2000))
      .endYear(Integer.valueOf(2020))
      .build();

    // Mocks
    Course courseMock = Course.builder()
      .id(egressCourseDTO.getCourseId())
      .build();

    Egress egressMock = Egress.builder()
      .id(egressCourseDTO.getEgressId())
      .build();

    EgressCourse egressCourseMock = new EgressCourse();

    // Mock behavior
    when(courseRepository.findById(egressCourseDTO.getCourseId()))
      .thenReturn(Optional.of(courseMock));
    when(egressRepository.findById(egressCourseDTO.getEgressId()))
      .thenReturn(Optional.of(egressMock));
    when(egressCourseRepository.findByEgressAndCourse(egressMock, courseMock))
      .thenReturn(Optional.of(egressCourseMock));

    // Test
    assertThrows(EgressAlreadyTakenTheCourseException.class, () -> {
      sut.registerEgressInCourse(egressCourseDTO);
    });
  }
}
