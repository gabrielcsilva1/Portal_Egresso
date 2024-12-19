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

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.egress.UpdateEgressDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.PositionRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.TestimonialRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressAlreadyExistsException;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressNotFoundException;

@ExtendWith(MockitoExtension.class)
public class EgressServiceTest {
  @InjectMocks
  private EgressService sut;

  @Mock
  private EgressRepository egressRepository;

  @Mock
  private PositionRepository positionRepository;

  @Mock
  private TestimonialRepository testimonialRepository;

  @Test
  @DisplayName("should be able to create a new egress.")
  public void create_egress_success() {
    // DTO
    EgressDTO egressDTO = EgressDTO.builder()
      .email("johnDoe@example.com")
      .name("John Doe")
      .build();
    
    // Mocks
    Egress mockEgress = Egress.builder()
      .id(UUID.randomUUID())
      .name(egressDTO.getName())
      .email(egressDTO.getEmail())
      .build();

    when(egressRepository.findByEmail(egressDTO.getEmail()))
      .thenReturn(Optional.empty());
    
    when(egressRepository.save(any(Egress.class)))
      .thenReturn(mockEgress);

    // Test
    Egress result = this.sut.createEgress(egressDTO);

    assertEquals(result.getId(), mockEgress.getId());
    assertEquals(result.getEmail(), egressDTO.getEmail());
    assertEquals(result.getName(), egressDTO.getName());
  }

  @Test
  @DisplayName("should not be able to create a new egress with a duplicate email address.")
  public void create_egress_email_already_exists_exception() {
    // DTO
    EgressDTO egressDTO = EgressDTO.builder()
      .email("johnDoe@example.com")
      .name("John Doe")
      .build();
    
    // Mocks
    Egress mockEgress = Egress.builder()
      .id(UUID.randomUUID())
      .name(egressDTO.getName())
      .email(egressDTO.getEmail())
      .build();

    when(egressRepository.findByEmail(egressDTO.getEmail()))
      .thenReturn(Optional.of(mockEgress));
    
    // Test
    assertThrows(EgressAlreadyExistsException.class, () -> {
      this.sut.createEgress(egressDTO);
    });
  }

  @Test
  @DisplayName("should be able to get a egress by id.")
  public void get_egress__by_id_success() {
    // Mocks
    Egress mockEgress = Egress.builder()
      .id(UUID.randomUUID())
      .name("John Doe")
      .email("johndoe@example.com")
      .build();

    when(egressRepository.findById(mockEgress.getId()))
      .thenReturn(Optional.of(mockEgress));

    // Test
    Egress result = sut.getEgressById(mockEgress.getId());

    assertNotNull(result);
    assertEquals(result.getId(), mockEgress.getId());
  }

  @Test
  @DisplayName("should not be able to get a egress with a invalid id.")
  public void get_egress__by_id_invalid_id() {
    // Id
    UUID invalidId = UUID.randomUUID();

    // Mocks
    when(egressRepository.findById(invalidId))
      .thenReturn(Optional.empty());

    // Test
    assertThrows(EgressNotFoundException.class, () -> sut.getEgressById(invalidId));
  }

  @Test
  @DisplayName("should be able to update a egress")
  public void update_egress_success() {
    UpdateEgressDTO egressDTO = UpdateEgressDTO.builder()
      .email("newEgress@example.com")
      .name("New name for Egress")
      .avatarUrl("new avatar")
      .curriculum("new curriculum")
      .description("new description")
      .linkedin("new linkedin")
      .instagram("new instagram")
      .build();
    
      Egress egressMock = Egress.builder()
        .id(UUID.randomUUID())
        .name("John Doe")
        .email("johndoe@example.com")
        .build();

      when(egressRepository.findById(egressMock.getId()))
        .thenReturn(Optional.of(egressMock));

      when(egressRepository.save(any(Egress.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

      Egress result = sut.updateEgress(egressMock.getId(), egressDTO);

      assertNotNull(result);
      assertEquals(egressDTO.getName(), result.getName());
      assertEquals(egressDTO.getEmail(), result.getEmail());
      assertEquals(egressDTO.getAvatarUrl(), result.getAvatarUrl());
      assertEquals(egressDTO.getCurriculum(), result.getCurriculum());
      assertEquals(egressDTO.getDescription(), result.getDescription());
      assertEquals(egressDTO.getLinkedin(), result.getLinkedin());
      assertEquals(egressDTO.getInstagram(), result.getInstagram());

  }

  @Test
  @DisplayName("should not be able to update a egress with invalid id")
  public void update_egress_with_invalid_id() {
    UpdateEgressDTO egressDTO = UpdateEgressDTO.builder()
      .email("newEgress@example.com")
      .name("New name for Egress")
      .build();

      when(egressRepository.findById(any(UUID.class)))
        .thenReturn(Optional.empty());

      assertThrows(EgressNotFoundException.class, () -> {
        sut.updateEgress(UUID.randomUUID(), egressDTO);
      });
  }
}
