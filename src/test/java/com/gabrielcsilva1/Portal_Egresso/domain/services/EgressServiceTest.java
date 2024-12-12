package com.gabrielcsilva1.Portal_Egresso.domain.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.PositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.TestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
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
  public void save_egress_success() {
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
  public void save_egress_email_already_exists_exception() {
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
  @DisplayName("should be able to create a new position")
  public void save_position_success() {
    // DTO
    PositionDTO positionDTO = PositionDTO.builder()
      .egressId(UUID.randomUUID())
      .description("Position Description")
      .location("Location Name")
      .startYear(2000)
      .endYear(2020)
      .build();

    // Mocks
    Egress egressMock = Egress.builder()
      .id(positionDTO.getEgressId())
      .build();
    
    Position positionMock = Position.builder()
      .id(UUID.randomUUID())
      .egress(egressMock)
      .description(positionDTO.getDescription())
      .location(positionDTO.getLocation())
      .startYear(positionDTO.getStartYear())
      .endYear(positionDTO.getEndYear())
      .build();

    when(this.egressRepository.findById(positionDTO.getEgressId()))
      .thenReturn(Optional.of(egressMock));

    when(this.positionRepository.save(any(Position.class)))
      .thenReturn(positionMock);

    // Test
    Position result = this.sut.registerEgressPosition(positionDTO);

    assertEquals(result.getId(), positionMock.getId());
    assertEquals(result.getEgress().getId(), positionDTO.getEgressId());
    assertEquals(result.getDescription(), positionDTO.getDescription());
  }

  @Test
  @DisplayName("should not be able to create a new position if the egress does not exist")
  public void save_position_egress_not_found() {
    // DTO
    PositionDTO positionDTO = PositionDTO.builder()
      .egressId(UUID.randomUUID())
      .description("Position Description")
      .location("Location Name")
      .startYear(2000)
      .endYear(2020)
      .build();

    // Mocks
    when(this.egressRepository.findById(positionDTO.getEgressId()))
      .thenReturn(Optional.empty());

    // Test
    assertThrows(EgressNotFoundException.class, () -> {
      sut.registerEgressPosition(positionDTO);
    });
  }

  @Test
  @DisplayName("should be able to create a egress testimonial.")
  public void register_egress_testimonial_success() {
    // DTO
    var testimonialDTO = TestimonialDTO.builder()
    .egressId(UUID.randomUUID())
    .text("testimonial test")
    .build();

    // Mocks
    var mockEgress = Egress.builder()
      .id(testimonialDTO.getEgressId())
      .name("John Doe")
      .email("johndoe@example.com")
      .build();
    
    var mockTestimonial = Testimonial.builder()
      .id(UUID.randomUUID())
      .egress(mockEgress)
      .text(testimonialDTO.getText())
      .createdAt(LocalDateTime.now())
      .build();

    when(this.egressRepository.findById(testimonialDTO.getEgressId()))
      .thenReturn(Optional.of(mockEgress));
    
    when(this.testimonialRepository.save(any(Testimonial.class)))
      .thenReturn(mockTestimonial);

    // Test
    Testimonial result = sut.registerEgressTestimonial(testimonialDTO);

    assertEquals(result.getId(), mockTestimonial.getId());
    assertEquals(result.getEgress().getId(), testimonialDTO.getEgressId());
    assertEquals(result.getCreatedAt(), mockTestimonial.getCreatedAt());
  }

  @Test
  @DisplayName("should not be able to create a testimonial from a egress that does not exist")
  public void save_testimonial_egress_not_found() {
    // DTO
    var testimonialDTO = TestimonialDTO.builder()
    .egressId(UUID.randomUUID())
    .text("testimonial test")
    .build();

    // Mocks
    when(this.egressRepository.findById(testimonialDTO.getEgressId()))
      .thenReturn(Optional.empty());

    // Test
    assertThrows(EgressNotFoundException.class, () -> {
      sut.registerEgressTestimonial(testimonialDTO);
    });
  }
}
