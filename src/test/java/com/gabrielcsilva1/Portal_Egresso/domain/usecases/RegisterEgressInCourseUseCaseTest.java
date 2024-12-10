package com.gabrielcsilva1.Portal_Egresso.domain.usecases;

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

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressCourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.EgressCourse;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressCourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions.CourseNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions.EgressNotFoundException;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions.InvalidEndYearException;

@ExtendWith(MockitoExtension.class)
public class RegisterEgressInCourseUseCaseTest {
  @InjectMocks
  private RegisterEgressInCourseUseCase sut;

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private EgressRepository egressRepository;

  @Mock
  private EgressCourseRepository egressCourseRepository;

  @Test
  @DisplayName("should be able to register a egress in a course")
  public void success() {
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
    EgressCourse result = sut.execute(egressCourseDTO);

    assertEquals(result.getId(), egressCourseMock.getId());
    assertEquals(result.getCourse().getId(), egressCourseDTO.getCourseId());
    assertEquals(result.getEgress().getId(), egressCourseDTO.getEgressId());
  }

  @Test
  @DisplayName("should not be able to register a invalid egress in a course")
  public void egressNotFound() {
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
      sut.execute(egressCourseDTO) ;
    });
  }

  @Test
  @DisplayName("should not be able to register a egress in a invalid course")
  public void courseNotFound() {
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
      sut.execute(egressCourseDTO);
    });
  }

  @Test
  @DisplayName("should not be able to register a egress in a course with invalid end year")
  public void invalidEndYear() {
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
      sut.execute(egressCourseDTO);
    });
  }
}
