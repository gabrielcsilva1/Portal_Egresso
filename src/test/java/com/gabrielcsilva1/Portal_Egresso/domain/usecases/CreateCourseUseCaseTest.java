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

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.CourseDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.usecases.exeptions.CoordinatorNotFoundException;

@ExtendWith(MockitoExtension.class)
public class CreateCourseUseCaseTest {
  @InjectMocks
  private CreateCourseUseCase sut;


  @Mock
  private CourseRepository courseRepository;

  @Mock
  private CoordinatorRepository coordinatorRepository;

  @Test
  @DisplayName("should be able to create a new course")
  public void success() {
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
    Course result = this.sut.execute(courseDTO);

    assertEquals(result.getId(), courseMock.getId());
    assertEquals(result.getCoordinator().getId(), courseDTO.getCoordinatorId());
  }

  @Test
  @DisplayName("should not be able to create a new course with a coordinator identifier not valid")
  public void coordinatorNotFound() {
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
      this.sut.execute(courseDTO);
   });
  }
}
