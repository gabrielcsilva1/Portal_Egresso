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
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.GraduateCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.course.UpdateCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.GraduateCourse;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateCourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.CoordinatorNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.CourseNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.GraduateAlreadyTakenTheCourseException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.GraduateNotFoundException;
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
  private GraduateRepository graduateRepository;

  @Mock
  private GraduateCourseRepository graduateCourseRepository;

  @Test
  @DisplayName("should be able to create a new course")
  public void create_course_success() {
    // DTO
    var courseDTO = CourseDTO.builder()
      .name("Course Name")
      .level("Graduation")
      .build();

    UUID coordinatorId = UUID.randomUUID();

    // Mocks
    var coordinatorMock = Coordinator.builder()
      .id(coordinatorId)
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
    Course result = this.sut.createCourse(courseDTO, coordinatorId);

    assertEquals(result.getId(), courseMock.getId());
    assertEquals(result.getCoordinator().getId(), coordinatorId);
  }

  @Test
  @DisplayName("should not be able to create a new course with a coordinator identifier not valid")
  public void create_course_coordinator_not_found_exception() {
     // DTO
     var courseDTO = CourseDTO.builder()
     .name("Course Name")
     .level("Graduation")
     .build();

     UUID coordinatorId = UUID.randomUUID();
    

   // Mocks
   when(this.coordinatorRepository.findById(coordinatorId))
     .thenReturn(Optional.empty());

   // Test
   assertThrows(CoordinatorNotFoundException.class, () -> {
      this.sut.createCourse(courseDTO, coordinatorId);
   });
  }

  @Test
  @DisplayName("should be able to register a graduate in a course")
  public void register_graduate_in_course_success() {
    // DTO
    var graduateCourseDTO = GraduateCourseDTO.builder()
      .graduateId(UUID.randomUUID())
      .courseId(UUID.randomUUID())
      .startYear(Integer.valueOf(2000))
      .endYear(Integer.valueOf(2020))
      .build();

    // Mocks
    Course courseMock = Course.builder()
      .id(graduateCourseDTO.getCourseId())
      .build();

    Graduate graduateMock = Graduate.builder()
      .id(graduateCourseDTO.getGraduateId())
      .build();

    GraduateCourse graduateCourseMock = GraduateCourse.builder()
      .id(UUID.randomUUID())
      .graduate(graduateMock)
      .course(courseMock)
      .startYear(graduateCourseDTO.getStartYear())
      .endYear(graduateCourseDTO.getEndYear())
      .build();

    // Mock behavior
    when(courseRepository.findById(graduateCourseDTO.getCourseId()))
      .thenReturn(Optional.of(courseMock));
    when(graduateRepository.findById(graduateCourseDTO.getGraduateId()))
      .thenReturn(Optional.of(graduateMock));
    when(graduateCourseRepository.save(any(GraduateCourse.class)))
      .thenReturn(graduateCourseMock);

    // Test
    GraduateCourse result = sut.registerGraduateInCourse(graduateCourseDTO);

    assertEquals(result.getId(), graduateCourseMock.getId());
    assertEquals(result.getCourse().getId(), graduateCourseDTO.getCourseId());
    assertEquals(result.getGraduate().getId(), graduateCourseDTO.getGraduateId());
  }

  @Test
  @DisplayName("should not be able to register a invalid graduate in a course")
  public void register_graduate_in_course_graduate_not_found() {
    // DTO
    var graduateCourseDTO = GraduateCourseDTO.builder()
      .graduateId(UUID.randomUUID())
      .courseId(UUID.randomUUID())
      .startYear(Integer.valueOf(2000))
      .endYear(Integer.valueOf(2020))
      .build();

    // Mock behavior
    when(graduateRepository.findById(graduateCourseDTO.getGraduateId()))
      .thenReturn(Optional.empty());

    // Test
    assertThrows(GraduateNotFoundException.class, () -> {
      sut.registerGraduateInCourse(graduateCourseDTO) ;
    });
  }

  @Test
  @DisplayName("should not be able to register a graduate in a invalid course")
  public void register_graduate_in_course_course_not_found() {
    // DTO
    var graduateCourseDTO = GraduateCourseDTO.builder()
      .graduateId(UUID.randomUUID())
      .courseId(UUID.randomUUID())
      .startYear(Integer.valueOf(2000))
      .endYear(Integer.valueOf(2020))
      .build();

    // Mocks
    Graduate graduateMock = Graduate.builder()
      .id(graduateCourseDTO.getGraduateId())
      .build();

    // Mock behavior
    when(graduateRepository.findById(graduateCourseDTO.getGraduateId()))
      .thenReturn(Optional.of(graduateMock));
    when(courseRepository.findById(graduateCourseDTO.getCourseId()))
      .thenReturn(Optional.empty());

    // Test
    assertThrows(CourseNotFoundException.class, () -> {
      sut.registerGraduateInCourse(graduateCourseDTO);
    });
  }

  @Test
  @DisplayName("should not be able to register an graduate in the course with an end year before the start year")
  public void register_graduate_in_course_invalid_end_year() {
    // DTO
    var graduateCourseDTO = GraduateCourseDTO.builder()
      .graduateId(UUID.randomUUID())
      .courseId(UUID.randomUUID())
      .startYear(Integer.valueOf(2000))
      .endYear(Integer.valueOf(1990))
      .build();

    // Mocks
    Course courseMock = Course.builder()
      .id(graduateCourseDTO.getCourseId())
      .build();

    Graduate graduateMock = Graduate.builder()
      .id(graduateCourseDTO.getGraduateId())
      .build();

    // Mock behavior
    when(courseRepository.findById(graduateCourseDTO.getCourseId()))
      .thenReturn(Optional.of(courseMock));
    when(graduateRepository.findById(graduateCourseDTO.getGraduateId()))
      .thenReturn(Optional.of(graduateMock));

    // Test
    assertThrows(InvalidEndYearException.class, () -> {
      sut.registerGraduateInCourse(graduateCourseDTO);
    });
  }

  @Test
  @DisplayName("should not be able to register an graduate twice in the same course")
  public void register_graduate_in_course_graduate_already_taken_the_course() {
    // DTO
    var graduateCourseDTO = GraduateCourseDTO.builder()
      .graduateId(UUID.randomUUID())
      .courseId(UUID.randomUUID())
      .startYear(Integer.valueOf(2000))
      .endYear(Integer.valueOf(2020))
      .build();

    // Mocks
    Course courseMock = Course.builder()
      .id(graduateCourseDTO.getCourseId())
      .build();

    Graduate graduateMock = Graduate.builder()
      .id(graduateCourseDTO.getGraduateId())
      .build();

    GraduateCourse graduateCourseMock = new GraduateCourse();

    // Mock behavior
    when(courseRepository.findById(graduateCourseDTO.getCourseId()))
      .thenReturn(Optional.of(courseMock));
    when(graduateRepository.findById(graduateCourseDTO.getGraduateId()))
      .thenReturn(Optional.of(graduateMock));
    when(graduateCourseRepository.findByGraduateAndCourse(graduateMock, courseMock))
      .thenReturn(Optional.of(graduateCourseMock));

    // Test
    assertThrows(GraduateAlreadyTakenTheCourseException.class, () -> {
      sut.registerGraduateInCourse(graduateCourseDTO);
    });
  }

  @Test
  @DisplayName("should be able to update a course")
  public void update_course_success() {
    UpdateCourseDTO courseDTO = UpdateCourseDTO.builder()
      .name("Ciências da computação")
      .level("Pós-graduação")
      .build();

    Course courseMock = Course.builder()
      .id(UUID.randomUUID())
      .name("Informática")
      .level("Graduação")
      .build();

    when(courseRepository.findById(courseMock.getId()))
      .thenReturn(Optional.of(courseMock));

    when(courseRepository.save(any(Course.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));

    Course result = sut.updateCourse(courseMock.getId(), courseDTO);

    assertEquals(result.getName(), courseMock.getName());
    assertEquals(result.getLevel(), courseDTO.getLevel());
  }

  @Test
  @DisplayName("should not be able to update a course with wrong id")
  public void update_course_with_invalid_course_id() {
    UpdateCourseDTO courseDTO = UpdateCourseDTO.builder()
      .name("Ciências da computação")
      .level("Pós-graduação")
      .build();

    when(courseRepository.findById(any(UUID.class)))
      .thenReturn(Optional.empty());

    assertThrows(CourseNotFoundException.class, () -> sut.updateCourse(UUID.randomUUID(), courseDTO));
  }
}
