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

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.TestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.TestimonialRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.exeptions.EgressNotFoundException;

@ExtendWith(MockitoExtension.class)
public class TestimonialServiceTest {
  @InjectMocks
  private TestimonialService sut;

  @Mock
  private EgressRepository egressRepository;

  @Mock
  private TestimonialRepository testimonialRepository;

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
  public void register_egress_testimonial_egress_not_found() {
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
