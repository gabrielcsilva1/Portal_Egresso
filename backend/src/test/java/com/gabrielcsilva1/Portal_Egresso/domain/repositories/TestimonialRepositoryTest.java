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
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;

@DataJpaTest
@ActiveProfiles("test")
public class TestimonialRepositoryTest {
  @Autowired
  private TestimonialRepository testimonialRepository;

  @Autowired
  private GraduateRepository graduateRepository;

  private Graduate graduate;

  @Test
  @DisplayName("should save a new testimonial")
  void save_new_testimonial() {
    graduate = Graduate.builder()
      .name("John Doe")
      .email("john.doe@example.com")
      .build();
    
    graduateRepository.save(graduate);

    Testimonial testimonial = testimonialRepository.save(Testimonial.builder()
            .graduate(graduate)
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
    Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));

    Page<Testimonial> testimonials = testimonialRepository.findByRegistrationStatus(StatusEnum.PENDING, pageable);
    
    assertThat(testimonials.getContent()).hasSize(3);
    assertThat(testimonials.getContent().get(0).getText()).isEqualTo("This is a recent testimonial.");
    assertThat(testimonials.getContent().get(1).getText()).isEqualTo("This is a testimonial from 2023.");
    assertThat(testimonials.getContent().get(2).getText()).isEqualTo("This is a testimonial from 2022.");
  }

  void populateTestimonial() {
    graduate = Graduate.builder()
      .name("John Doe")
      .email("john.doe@example.com")
      .password("123456")
      .build();
    
    graduateRepository.save(graduate);

    testimonialRepository.save(Testimonial.builder()
            .graduate(graduate)
            .text("This is a testimonial from 2023.")
            .createdAt(LocalDateTime.of(2023, 5, 10, 12, 0))
            .build());
    
    testimonialRepository.save(Testimonial.builder()
            .graduate(graduate)
            .text("This is a testimonial from 2022.")
            .createdAt(LocalDateTime.of(2022, 5, 10, 12, 0))
            .build());

    testimonialRepository.save(Testimonial.builder()
            .graduate(graduate)
            .text("This is a recent testimonial.")
            .build());
  }
}
