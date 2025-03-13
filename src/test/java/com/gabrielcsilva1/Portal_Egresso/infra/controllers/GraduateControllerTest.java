package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Course;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.IGenericUser;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CourseRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TokenService;
import com.gabrielcsilva1.Portal_Egresso.dtos.paginated.ResponsePaginated;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.graduate.RequestCreateGraduateJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.graduate.RequestUpdateGraduateJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.graduate.ResponseGraduateJson;
import com.gabrielcsilva1.Portal_Egresso.dtos.response.graduate.ResponseShortGraduateJson;
import com.gabrielcsilva1.utils.faker.FakeCoordinatorFactory;
import com.gabrielcsilva1.utils.faker.FakeCourseFactory;
import com.gabrielcsilva1.utils.faker.FakeGraduateFactory;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class GraduateControllerTest {
  @Autowired
  private GraduateRepository graduateRepository;

  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private TokenService tokenService;

  private Cookie authenticateUser(String subject, IGenericUser user) {
    var jwtToken = tokenService.generateToken(subject, user.getRoles());

    return new Cookie("jwtToken", jwtToken);
  }

  @Test
  public void fetch_graduates_controller_success() throws Exception {
    Graduate graduateInDatabase1 = FakeGraduateFactory.makeGraduate();

    Graduate graduateInDatabase2 = FakeGraduateFactory.makeGraduate();

    graduateRepository.save(graduateInDatabase1);
    graduateRepository.save(graduateInDatabase2);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.get("/graduate")
    );

    String jsonResponse = result.andReturn().getResponse().getContentAsString();
    ResponsePaginated<ResponseShortGraduateJson> fetchGraduatesResponse = objectMapper.readValue(
      jsonResponse,
      new TypeReference<ResponsePaginated<ResponseShortGraduateJson>>(){}
    );

    assertThat(fetchGraduatesResponse.getContent()).isNotNull();
    assertThat(fetchGraduatesResponse.getContent()).hasSize(2);
  }

  @Test
  public void create_graduate_controller_success() throws Exception {
    Coordinator coordinator = FakeCoordinatorFactory.makeCoordinator();
    coordinator = coordinatorRepository.save(coordinator);

    Course courseInDatabase = FakeCourseFactory.makeCourse(coordinator);
    courseInDatabase = courseRepository.save(courseInDatabase);

    RequestCreateGraduateJson graduateDTO = RequestCreateGraduateJson.builder()
      .name("John Doe")
      .email("johndoe@example.com")
      .password("123456")
      .courseId(courseInDatabase.getId())
      .startYear(2000)
      .build();

    var result = mockMvc.perform(
      MockMvcRequestBuilders.post("/graduate")
       .contentType(MediaType.APPLICATION_JSON)
       .content(objectMapper.writeValueAsString(graduateDTO))
    );

    // Teste
    result.andExpect(MockMvcResultMatchers.status().isCreated());

    List<Graduate> graduatesOnDatabase = graduateRepository.findAll();

    assertThat(graduatesOnDatabase).hasSize(1);
  }

  @Test
  public void get_graduate_by_id_controller_success() throws Exception {
    Graduate graduateInDatabase = FakeGraduateFactory.makeGraduate();

    graduateInDatabase = graduateRepository.save(graduateInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.get("/graduate/{id}", graduateInDatabase.getId())
    );

    String jsonResponse = result.andReturn().getResponse().getContentAsString();
    ResponseGraduateJson getGraduateResponse = objectMapper.readValue(
      jsonResponse,
      ResponseGraduateJson.class
    );

    assertThat(getGraduateResponse).isNotNull();
    assertThat(getGraduateResponse.getName()).isEqualTo(graduateInDatabase.getName());
    assertThat(getGraduateResponse.getEmail()).isEqualTo(graduateInDatabase.getEmail());
  }

  @Test
  public void update_graduate_by_id_controller_success() throws Exception {
    RequestUpdateGraduateJson graduateDTO = RequestUpdateGraduateJson.builder()
      .name("Update")
      .email("update@example.com")
      .avatarUrl("https://avatarUrlUpdated.png")
      .curriculum("curriculumUpdated")
      .description("descriptionUpdated")
      .instagram("instagramUpdated")
      .linkedin("linkedinUpdated")
      .build();

    Graduate graduateInDatabase = FakeGraduateFactory.makeGraduate();

    graduateInDatabase = graduateRepository.save(graduateInDatabase);
      
    var cookie = this.authenticateUser(graduateInDatabase.getId().toString(), graduateInDatabase);
      
    var result = mockMvc.perform(
      MockMvcRequestBuilders.put("/graduate/{id}", graduateInDatabase.getId())
       .contentType(MediaType.APPLICATION_JSON)
       .cookie(cookie)
       .content(objectMapper.writeValueAsString(graduateDTO))
    );

    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    graduateInDatabase = graduateRepository.findById(graduateInDatabase.getId()).get();

    assertThat(graduateInDatabase).isNotNull();
    assertThat(graduateInDatabase.getName()).isEqualTo(graduateDTO.getName());
    assertThat(graduateInDatabase.getEmail()).isEqualTo(graduateDTO.getEmail());
    assertThat(graduateInDatabase.getAvatarUrl()).isEqualTo(graduateDTO.getAvatarUrl());
    assertThat(graduateInDatabase.getCurriculum()).isEqualTo(graduateDTO.getCurriculum());
    assertThat(graduateInDatabase.getDescription()).isEqualTo(graduateDTO.getDescription());
    assertThat(graduateInDatabase.getInstagram()).isEqualTo(graduateDTO.getInstagram());
    assertThat(graduateInDatabase.getLinkedin()).isEqualTo(graduateDTO.getLinkedin());
  }

  @Test
  public void delete_graduate_by_id_controller_success() throws Exception {
    Coordinator coordinatorInDatabase = FakeCoordinatorFactory.makeCoordinator();
    coordinatorInDatabase = coordinatorRepository.save(coordinatorInDatabase);

    var cookie = this.authenticateUser(
      coordinatorInDatabase.getId().toString(), 
      coordinatorInDatabase
      );

    Graduate graduateInDatabase = FakeGraduateFactory.makeGraduate();

    graduateInDatabase = graduateRepository.save(graduateInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.delete("/graduate/{id}", graduateInDatabase.getId())
      .cookie(cookie)
    );

    // Teste

    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    List<Graduate> rowsOnDatabase = graduateRepository.findAll();

    assertThat(rowsOnDatabase).hasSize(0);
  }
}
