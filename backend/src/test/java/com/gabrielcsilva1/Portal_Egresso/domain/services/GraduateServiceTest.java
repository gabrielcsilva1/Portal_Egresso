package com.gabrielcsilva1.Portal_Egresso.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.GraduateCourse;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.PositionRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.TestimonialRepository;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.graduate.RequestCreateGraduateJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.graduate.RequestUpdateGraduateJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.graduateCourse.RequestGraduateCourseJson;
import com.gabrielcsilva1.Portal_Egresso.exeptions.GraduateAlreadyExistsException;
import com.gabrielcsilva1.Portal_Egresso.exeptions.GraduateNotFoundException;

@ExtendWith(MockitoExtension.class)
public class GraduateServiceTest {
  @InjectMocks
  private GraduateService sut;

  @Mock
  private GraduateRepository graduateRepository;

  @Mock
  private CourseService courseService;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private PositionRepository positionRepository;

  @Mock
  private TestimonialRepository testimonialRepository;

  @Test
  @DisplayName("should be able to create a new graduate.")
  public void create_graduate_success() {
    // DTO
    RequestCreateGraduateJson graduateDTO = RequestCreateGraduateJson.builder()
      .email("johnDoe@example.com")
      .name("John Doe")
      .startYear(2004)
      .build();
    
    // Mocks
    Graduate mockGraduate = Graduate.builder()
      .id(UUID.randomUUID())
      .name(graduateDTO.getName())
      .email(graduateDTO.getEmail())
      .build();

    GraduateCourse mockGraduateCourse = GraduateCourse.builder()
      .id(UUID.randomUUID())
      .graduate(mockGraduate)
      .build();

    when(graduateRepository.findByEmail(graduateDTO.getEmail()))
      .thenReturn(Optional.empty());
    
    when(graduateRepository.save(any(Graduate.class)))
      .thenReturn(mockGraduate);
    
    when(courseService.registerGraduateInCourse(any(RequestGraduateCourseJson.class)))
      .thenReturn(mockGraduateCourse);

    // Test
    Graduate result = this.sut.createGraduate(graduateDTO);

    assertEquals(result.getId(), mockGraduate.getId());
    assertEquals(result.getEmail(), graduateDTO.getEmail());
    assertEquals(result.getName(), graduateDTO.getName());
  }

  @Test
  @DisplayName("should not be able to create a new graduate with a duplicate email address.")
  public void create_graduate_email_already_exists_exception() {
    // DTO
    RequestCreateGraduateJson graduateDTO = RequestCreateGraduateJson.builder()
      .email("johnDoe@example.com")
      .name("John Doe")
      .build();
    
    // Mocks
    Graduate mockGraduate = Graduate.builder()
      .id(UUID.randomUUID())
      .name(graduateDTO.getName())
      .email(graduateDTO.getEmail())
      .build();

    when(graduateRepository.findByEmail(graduateDTO.getEmail()))
      .thenReturn(Optional.of(mockGraduate));
    
    // Test
    assertThrows(GraduateAlreadyExistsException.class, () -> {
      this.sut.createGraduate(graduateDTO);
    });
  }

  @Test
  @DisplayName("should be able to get a graduate by id.")
  public void get_graduate__by_id_success() {
    // Mocks
    Graduate mockGraduate = Graduate.builder()
      .id(UUID.randomUUID())
      .name("John Doe")
      .email("johndoe@example.com")
      .build();

    when(graduateRepository.findById(mockGraduate.getId()))
      .thenReturn(Optional.of(mockGraduate));

    // Test
    Graduate result = sut.getGraduateById(mockGraduate.getId());

    assertNotNull(result);
    assertEquals(result.getId(), mockGraduate.getId());
  }

  @Test
  @DisplayName("should not be able to get a graduate with a invalid id.")
  public void get_graduate__by_id_invalid_id() {
    // Id
    UUID invalidId = UUID.randomUUID();

    // Mocks
    when(graduateRepository.findById(invalidId))
      .thenReturn(Optional.empty());

    // Test
    assertThrows(GraduateNotFoundException.class, () -> sut.getGraduateById(invalidId));
  }

  @Test
  @DisplayName("should be able to update a graduate")
  public void update_graduate_success() {
    RequestUpdateGraduateJson graduateDTO = RequestUpdateGraduateJson.builder()
      .email("newGraduate@example.com")
      .name("New name for Graduate")
      .avatarUrl("new avatar")
      .curriculum("new curriculum")
      .description("new description")
      .linkedin("new linkedin")
      .instagram("new instagram")
      .build();
    
      Graduate graduateMock = Graduate.builder()
        .id(UUID.randomUUID())
        .name("John Doe")
        .email("johndoe@example.com")
        .build();

      when(graduateRepository.findById(graduateMock.getId()))
        .thenReturn(Optional.of(graduateMock));

      when(graduateRepository.save(any(Graduate.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

      Graduate result = sut.updateGraduate(graduateMock.getId(), graduateDTO);

      assertNotNull(result);
      assertEquals(graduateDTO.getName(), result.getName());
      assertEquals(graduateDTO.getEmail(), result.getEmail());
      assertEquals(graduateDTO.getAvatarUrl(), result.getAvatarUrl());
      assertEquals(graduateDTO.getCurriculum(), result.getCurriculum());
      assertEquals(graduateDTO.getDescription(), result.getDescription());
      assertEquals(graduateDTO.getLinkedin(), result.getLinkedin());
      assertEquals(graduateDTO.getInstagram(), result.getInstagram());

  }

  @Test
  @DisplayName("should not be able to update a graduate with invalid id")
  public void update_graduate_with_invalid_id() {
    RequestUpdateGraduateJson graduateDTO = RequestUpdateGraduateJson.builder()
      .email("newGraduate@example.com")
      .name("New name for Graduate")
      .build();

      when(graduateRepository.findById(any(UUID.class)))
        .thenReturn(Optional.empty());

      assertThrows(GraduateNotFoundException.class, () -> {
        sut.updateGraduate(UUID.randomUUID(), graduateDTO);
      });
  }
}
