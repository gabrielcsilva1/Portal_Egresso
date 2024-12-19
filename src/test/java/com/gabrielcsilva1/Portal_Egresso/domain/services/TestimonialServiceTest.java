package com.gabrielcsilva1.Portal_Egresso.domain.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

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

  @Test
  @DisplayName("should be able to fetch all recent testimonials")
  public void fetch_testimonials_order_by_created_at_desc() {
    // Mocks
    PageRequest pageRequest = PageRequest.of(0, 10);

    Egress egressMock = Egress.builder()
      .id(UUID.randomUUID())
      .name("John Doe")
      .email("john.doe@example.com")
      .build();

    List<Testimonial> testimonialsMock = List.of(
      Testimonial.builder()
      .egress(egressMock)
      .text("testimonial test")
      .build()
    );

    Page<Testimonial> mockPageTestimonial = new PageImpl<Testimonial>(testimonialsMock);

    when(this.testimonialRepository.findAllByOrderByCreatedAtDesc(pageRequest))
      .thenReturn(mockPageTestimonial);

    // Test

    Page<Testimonial> result = sut.fetchTestimonials(null, 0, 10);
    
    assertNotNull(result);
    assertThat(result.getContent()).hasSize(1);
  }

  @Test
  @DisplayName("should be able to fetch testimonials by year")
  public void fetch_testimonials_by_year() {
    // Mocks
    PageRequest pageRequest = PageRequest.of(0, 10);

    Egress egressMock = Egress.builder()
      .id(UUID.randomUUID())
      .name("John Doe")
      .email("john.doe@example.com")
      .build();

    List<Testimonial> testimonialsMock = List.of(
      Testimonial.builder()
      .egress(egressMock)
      .text("testimonial test 2022")
      .createdAt(LocalDateTime.of(2022, 1, 1, 0, 0, 0))
      .build()
    );

    Page<Testimonial> mockPageTestimonial = new PageImpl<Testimonial>(testimonialsMock);

    when(this.testimonialRepository.findByYear(2022, pageRequest))
      .thenReturn(mockPageTestimonial);

    // Test

    Page<Testimonial> result = sut.fetchTestimonials(2022, 0, 10);
    
    assertNotNull(result);
    assertThat(result.getContent()).hasSize(1);
    assertThat(result.getContent().get(0).getText()).isEqualTo("testimonial test 2022");
  }

  @Test
  @DisplayName("should be able to update a egress testimonial")
  public void update_egress_testimonial_success() {
    String newText = "testimonial text";

    Testimonial testimonialMock = Testimonial.builder()
      .id(UUID.randomUUID())
      .build();
    
    when(testimonialRepository.findById(any(UUID.class)))
      .thenReturn(Optional.of(testimonialMock));

    when(testimonialRepository.save(any(Testimonial.class)))
      .thenAnswer(invocation -> invocation.getArgument(0));
    
    Testimonial result = sut.updateEgressTestimonial(testimonialMock.getId(), newText);

    assertEquals(result.getText(), newText);
  }
}
