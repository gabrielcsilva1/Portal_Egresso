package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.IGenericUser;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Testimonial;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.TestimonialRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TokenService;
import com.gabrielcsilva1.Portal_Egresso.dtos.paginated.ResponsePaginated;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.testimonial.RequestCreateTestimonialJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.testimonial.RequestUpdateTestimonialJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.testimonial.ResponseTestimonialJson;
import com.gabrielcsilva1.utils.faker.FakeGraduateFactory;
import com.gabrielcsilva1.utils.faker.FakeTestimonial;

import jakarta.servlet.http.Cookie;


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
  private GraduateRepository graduateRepository;

  @Autowired
  private TestimonialRepository testimonialRepository;


  private Graduate graduate;

  @BeforeEach
  public void setup() {
    Graduate graduateInDatabase = FakeGraduateFactory.makeGraduate();
    this.graduate = graduateRepository.save(graduateInDatabase);
  }

  @AfterEach
  public void clean() {
    graduateRepository.deleteAll();
  }

  private Cookie authenticateUser(String subject, IGenericUser user) {
    String accessToken = tokenService.generateToken(subject, user.getRoles());
    return  new Cookie("jwtToken", accessToken);
  }

  @Test
  public void register_graduate_testimonial() throws Exception {
    Cookie cookie = authenticateUser(
      graduate.getId().toString(),
      graduate
    );

    RequestCreateTestimonialJson testimonialDTO = RequestCreateTestimonialJson.builder()
      .text("Testimonial")
      .build();

    var result = mockMvc.perform(
      MockMvcRequestBuilders.post("/testimonial")
      .cookie(cookie)
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(testimonialDTO))
    );

    result.andExpect(MockMvcResultMatchers.status().isCreated());

    List<Testimonial> testimonialsInDatabase = testimonialRepository.findAll();

    assertThat(testimonialsInDatabase).hasSize(1);
  }

  @Test
  public void fetch_graduate_testimonials() throws Exception {
    Testimonial testimonialInDatabase1 = FakeTestimonial.makeTestimonial(graduate);

    Testimonial testimonialInDatabase2 = FakeTestimonial.makeTestimonial(graduate);
    
    testimonialInDatabase1 = testimonialRepository.save(testimonialInDatabase1);
    testimonialInDatabase2 = testimonialRepository.save(testimonialInDatabase2);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.get("/testimonial")
    );

    String jsonResponse = result.andReturn().getResponse().getContentAsString();
    ResponsePaginated<ResponseTestimonialJson> fetchTestimonialsResponse = objectMapper.readValue(
      jsonResponse,
      new TypeReference<ResponsePaginated<ResponseTestimonialJson>>(){}
    );

    assertThat(fetchTestimonialsResponse.getContent()).isNotNull();
    assertThat(fetchTestimonialsResponse.getContent()).hasSize(2);
    
  }

  @Test
  public void update_graduate_testimonial() throws Exception {
    Cookie cookie = authenticateUser(
      graduate.getId().toString(),
      graduate
    );

    Testimonial testimonialInDatabase = FakeTestimonial.makeTestimonial(graduate);
    
    testimonialInDatabase = testimonialRepository.save(testimonialInDatabase);

    RequestUpdateTestimonialJson testimonialDTO = RequestUpdateTestimonialJson.builder()
      .text("New text")
      .build();

    var result = mockMvc.perform(
      MockMvcRequestBuilders.put("/testimonial/{id}", testimonialInDatabase.getId())
      .cookie(cookie)
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(testimonialDTO))
    );

    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    testimonialInDatabase = testimonialRepository.findById(testimonialInDatabase.getId()).orElse(null);

    assertThat(testimonialInDatabase.getText()).isEqualTo(testimonialDTO.getText());
  }

  @Test
  public void delete_graduate_testimonial() throws Exception {
    Cookie cookie = authenticateUser(
      graduate.getId().toString(),
      graduate
    );

    Testimonial testimonialInDatabase = FakeTestimonial.makeTestimonial(graduate);
    
    testimonialInDatabase = testimonialRepository.save(testimonialInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.delete("/testimonial/{id}", testimonialInDatabase.getId())
      .cookie(cookie)
    );

    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    Optional<Testimonial> databaseResponse = testimonialRepository.findById(testimonialInDatabase.getId());

    assertThat(databaseResponse.isEmpty()).isTrue();
  }
}
