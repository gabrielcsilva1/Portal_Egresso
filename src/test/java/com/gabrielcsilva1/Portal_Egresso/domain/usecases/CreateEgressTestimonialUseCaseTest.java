package com.gabrielcsilva1.Portal_Egresso.domain.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.TestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.EgressRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.TestimonialRepository;
import com.gabrielcsilva1.Portal_Egresso.exeptions.EgressNotFoundException;

public class CreateEgressTestimonialUseCaseTest {
  @InjectMocks
  private CreateEgressTestimonialUseCase createEgressTestimonialUseCase;

  @Mock
  private TestimonialRepository testimonialRepository;

  @Mock
  private EgressRepository egressRepository;

  @BeforeEach
  public void setup() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("should be able to create a egress testimonial.")
  public void sucess() {
    var testimonialDTO = TestimonialDTO.builder()
    .egressId(UUID.randomUUID())
    .text("testimonial test")
    .build();

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

    var result = this.createEgressTestimonialUseCase.execute(testimonialDTO);

    assertEquals(result.getId(), mockTestimonial.getId());
    assertEquals(result.getEgress(), mockTestimonial.getEgress());
    assertEquals(result.getCreatedAt(), mockTestimonial.getCreatedAt());
  }

  @Test
  @DisplayName("should not be able to create a testimonial from a egress that does not exist")
  public void egressNotFound() {
    var testimonialDTO = TestimonialDTO.builder()
    .egressId(UUID.randomUUID())
    .text("testimonial test")
    .build();

    when(this.egressRepository.findById(testimonialDTO.getEgressId()))
      .thenReturn(Optional.empty());

    assertThrows(EgressNotFoundException.class, () -> {
      this.createEgressTestimonialUseCase.execute(testimonialDTO);
    });
  }

}
