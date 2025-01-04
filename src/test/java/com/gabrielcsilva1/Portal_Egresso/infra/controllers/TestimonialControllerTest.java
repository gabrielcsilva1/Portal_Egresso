package com.gabrielcsilva1.Portal_Egresso.infra.controllers;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.TestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.paginated.PaginatedResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.testimonial.GetTestimonialResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.testimonial.UpdateTestimonialDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.TestimonialRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TokenService;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class TestimonialControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TokenService tokenService;

  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @Autowired
  private GraduateRepository graduateRepository;

  @Autowired
  private TestimonialRepository testimonialRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private Coordinator coordinator;

  private Graduate graduate;

  @BeforeEach
  public void setup() {
    Coordinator coordinatorToSave = Coordinator.builder()
      .login("admin")
      .password(passwordEncoder.encode("admin"))
      .build();

    Graduate graduateToSave = Graduate.builder()
      .name("John Doe")
      .email("john.doe@example.com")
      .build();

    this.coordinator = coordinatorRepository.save(coordinatorToSave);
    this.graduate = graduateRepository.save(graduateToSave);
  }

  @AfterEach
  public void clean() {
    coordinatorRepository.deleteAll();
    graduateRepository.deleteAll();
  }

  @Test
  public void register_graduate_testimonial() throws Exception {
    String accessToken = tokenService.generateToken(coordinator);

    TestimonialDTO testimonialDTO = TestimonialDTO.builder()
      .graduateId(graduate.getId())
      .text("Testimonial")
      .build();

    var result = mockMvc.perform(
      MockMvcRequestBuilders.post("/graduate/testimonial")
      .header("Authorization", "Bearer " + accessToken)
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(testimonialDTO))
    );

    result.andExpect(MockMvcResultMatchers.status().isCreated());

    List<Testimonial> testimonialsInDatabase = testimonialRepository.findAll();

    assertThat(testimonialsInDatabase).hasSize(1);
  }

  @Test
  public void fetch_graduate_testimonials() throws Exception {
    Testimonial testimonialInDatabase1 = Testimonial.builder()
      .graduate(graduate)
      .text("Testimonial 1")
      .createdAt(LocalDateTime.now())
      .build();

    Testimonial testimonialInDatabase2 = Testimonial.builder()
      .graduate(graduate)
      .text("Testimonial 2")
      .createdAt(LocalDateTime.now())
      .build();
    
    testimonialInDatabase1 = testimonialRepository.save(testimonialInDatabase1);
    testimonialInDatabase2 = testimonialRepository.save(testimonialInDatabase2);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.get("/graduate/testimonial")
    );

    String jsonResponse = result.andReturn().getResponse().getContentAsString();
    PaginatedResponse<GetTestimonialResponse> fetchTestimonialsResponse = objectMapper.readValue(
      jsonResponse,
      new TypeReference<PaginatedResponse<GetTestimonialResponse>>(){}
    );

    assertThat(fetchTestimonialsResponse.getContent()).isNotNull();
    assertThat(fetchTestimonialsResponse.getContent()).hasSize(2);
    
  }

  @Test
  public void update_graduate_testimonial() throws Exception {
    String accessToken = tokenService.generateToken(coordinator);

    Testimonial testimonialInDatabase = Testimonial.builder()
      .graduate(graduate)
      .text("Testimonial")
      .createdAt(LocalDateTime.now())
      .build();
    
    testimonialInDatabase = testimonialRepository.save(testimonialInDatabase);

    UpdateTestimonialDTO testimonialDTO = UpdateTestimonialDTO.builder()
      .text("New text")
      .build();

    var result = mockMvc.perform(
      MockMvcRequestBuilders.put("/graduate/testimonial/{id}", testimonialInDatabase.getId())
      .header("Authorization", "Bearer " + accessToken)
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(testimonialDTO))
    );

    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    testimonialInDatabase = testimonialRepository.findById(testimonialInDatabase.getId()).orElse(null);

    assertThat(testimonialInDatabase.getText()).isEqualTo(testimonialDTO.getText());
  }

  @Test
  public void delete_graduate_testimonial() throws Exception {
    String accessToken = tokenService.generateToken(coordinator);

    Testimonial testimonialInDatabase = Testimonial.builder()
      .graduate(graduate)
      .text("Testimonial")
      .createdAt(LocalDateTime.now())
      .build();
    
    testimonialInDatabase = testimonialRepository.save(testimonialInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.delete("/graduate/testimonial/{id}", testimonialInDatabase.getId())
      .header("Authorization", "Bearer " + accessToken)
    );

    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    Optional<Testimonial> databaseResponse = testimonialRepository.findById(testimonialInDatabase.getId());

    assertThat(databaseResponse.isEmpty()).isTrue();
  }
}
