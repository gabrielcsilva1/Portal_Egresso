package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;

@DataJpaTest
@ActiveProfiles("test")
public class TestimonialRepositoryTest {
  @Autowired
  private TestimonialRepository testimonialRepository;

  @Autowired
  private EgressRepository egressRepository;

  private Egress egress;

  @Test
  @DisplayName("should save a new testimonial")
  void save_new_testimonial() {
    egress = Egress.builder()
      .name("John Doe")
      .email("john.doe@example.com")
      .build();
    
    egressRepository.save(egress);

    Testimonial testimonial = testimonialRepository.save(Testimonial.builder()
            .egress(egress)
            .text("Test testimonial")
            .build());
    
    assertNotNull(testimonial.getId());
    assertEquals(testimonial.getText(), "Test testimonial");
  }

  @Test
  @DisplayName("should find testimonials by year")
  void find_testimonials_by_year() {
      this.populateTestimonial();

      PageRequest pageRequest = PageRequest.of(0, 10);

      Page<Testimonial> testimonials2023 = testimonialRepository.findByYear(2023, pageRequest);

      assertThat(testimonials2023.getContent()).hasSize(1);
      assertThat(testimonials2023.getContent().get(0).getText()).isEqualTo("This is a testimonial from 2023.");
  }

  @Test
  @DisplayName("should find all testimonials ordered by created at desc")
  public void find_all_by_order_by_created_at_desc() {
    this.populateTestimonial();
    Pageable pageable = PageRequest.of(0, 10);

    Page<Testimonial> testimonials = testimonialRepository.findAllByOrderByCreatedAtDesc(pageable);
    
    assertThat(testimonials.getContent()).hasSize(3);
    assertThat(testimonials.getContent().get(0).getText()).isEqualTo("This is a recent testimonial.");
    assertThat(testimonials.getContent().get(1).getText()).isEqualTo("This is a testimonial from 2023.");
    assertThat(testimonials.getContent().get(2).getText()).isEqualTo("This is a testimonial from 2022.");
  }

  void populateTestimonial() {
    egress = Egress.builder()
      .name("John Doe")
      .email("john.doe@example.com")
      .build();
    
    egressRepository.save(egress);

    testimonialRepository.save(Testimonial.builder()
            .egress(egress)
            .text("This is a testimonial from 2023.")
            .createdAt(LocalDateTime.of(2023, 5, 10, 12, 0))
            .build());
    
    testimonialRepository.save(Testimonial.builder()
            .egress(egress)
            .text("This is a testimonial from 2022.")
            .createdAt(LocalDateTime.of(2022, 5, 10, 12, 0))
            .build());

    testimonialRepository.save(Testimonial.builder()
            .egress(egress)
            .text("This is a recent testimonial.")
            .build());
  }
}
