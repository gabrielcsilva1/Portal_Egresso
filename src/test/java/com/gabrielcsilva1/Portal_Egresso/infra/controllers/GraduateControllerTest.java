package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Set;

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
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.GraduateDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate.FetchGraduateResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate.GraduateResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.graduate.UpdateGraduateDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.paginated.PaginatedResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TokenService;

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
  private TokenService tokenService;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  private String createCoordinatorAndAuthenticate() {
    var coordinator = Coordinator.builder()
     .login("admin")
     .password("password")
     .build();

    coordinator = coordinatorRepository.save(coordinator);

    String accessToken = tokenService.generateToken(coordinator);
    return accessToken;
  }


  @Test
  public void fetch_graduates_controller_success() throws Exception {
    Graduate graduateInDatabase1 = Graduate.builder()
      .name("John Doe 1")
      .email("johndoe1@example.com")
      .graduateCourses(Set.of())
      .positions(Set.of())
      .build();

    Graduate graduateInDatabase2 = Graduate.builder()
      .name("John Doe 2")
      .email("johndoe2@example.com")
      .graduateCourses(Set.of())
      .positions(Set.of())
      .build();

    graduateRepository.save(graduateInDatabase1);
    graduateRepository.save(graduateInDatabase2);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.get("/graduate")
    );

    String jsonResponse = result.andReturn().getResponse().getContentAsString();
    PaginatedResponse<FetchGraduateResponse> fetchGraduatesResponse = objectMapper.readValue(
      jsonResponse,
      new TypeReference<PaginatedResponse<FetchGraduateResponse>>(){}
    );

    assertThat(fetchGraduatesResponse.getContent()).isNotNull();
    assertThat(fetchGraduatesResponse.getContent()).hasSize(2);
  }

  @Test
  public void create_graduate_controller_success() throws Exception {
    String accessToken = createCoordinatorAndAuthenticate();

    GraduateDTO graduateDTO = GraduateDTO.builder()
      .name("John Doe")
      .email("johndoe@example.com")
      .build();

    var result = mockMvc.perform(
      MockMvcRequestBuilders.post("/graduate")
       .header("Authorization", "Bearer " + accessToken)
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
    Graduate graduateInDatabase = Graduate.builder()
      .name("John Doe")
      .email("johndoe@example.com")
      .graduateCourses(Set.of())
      .positions(Set.of())
      .build();

    graduateInDatabase = graduateRepository.save(graduateInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.get("/graduate/{id}", graduateInDatabase.getId())
    );

    String jsonResponse = result.andReturn().getResponse().getContentAsString();
    GraduateResponse getGraduateResponse = objectMapper.readValue(
      jsonResponse,
      GraduateResponse.class
    );

    assertThat(getGraduateResponse).isNotNull();
    assertThat(getGraduateResponse.getName()).isEqualTo(graduateInDatabase.getName());
    assertThat(getGraduateResponse.getEmail()).isEqualTo(graduateInDatabase.getEmail());
  }

  @Test
  public void update_graduate_by_id_controller_success() throws Exception {
    String accessToken = createCoordinatorAndAuthenticate();

    UpdateGraduateDTO graduateDTO = UpdateGraduateDTO.builder()
      .name("Update")
      .email("update@example.com")
      .avatarUrl("https://avatarUrlUpdated.png")
      .curriculum("curriculumUpdated")
      .description("descriptionUpdated")
      .instagram("instagramUpdated")
      .linkedin("linkedinUpdated")
      .build();

    Graduate graduateInDatabase = Graduate.builder()
      .name("John Doe")
      .email("johndoe@example.com")
      .graduateCourses(Set.of())
      .positions(Set.of())
      .build();

    graduateInDatabase = graduateRepository.save(graduateInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.put("/graduate/{id}", graduateInDatabase.getId())
       .contentType(MediaType.APPLICATION_JSON)
       .header("Authorization", "Bearer " + accessToken)
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
    String accessToken = createCoordinatorAndAuthenticate();

    Graduate graduateInDatabase = Graduate.builder()
      .name("John Doe")
      .email("johndoe@example.com")
      .build();

    graduateInDatabase = graduateRepository.save(graduateInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.delete("/graduate/{id}", graduateInDatabase.getId())
      .header("Authorization", "Bearer " + accessToken)
    );

    // Teste

    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    List<Graduate> rowsOnDatabase = graduateRepository.findAll();

    assertThat(rowsOnDatabase).hasSize(0);
  }
}
