package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.PositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.position.UpdatePositionDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.GraduateRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.PositionRepository;
import com.gabrielcsilva1.Portal_Egresso.domain.services.TokenService;

import jakarta.servlet.http.Cookie;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class PositionControllerTest {
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
  private PositionRepository positionRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  private Coordinator coordinator;

  private Graduate graduate;

  private Cookie cookie;

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

  private void authenticateUser() {
    String accessToken = tokenService.generateToken(coordinator.getId().toString());
    cookie = new Cookie("jwtToken", accessToken);
  }

  @Test
  public void register_graduate_position_controller_success() throws Exception{
    authenticateUser();

    PositionDTO positionDTO = PositionDTO.builder()
      .graduateId(graduate.getId())
      .location("Location")
      .description("Description")
      .startYear(2005)
      .build();

    var result = mockMvc.perform(
      MockMvcRequestBuilders.post("/graduate/position")
      .cookie(cookie)
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(positionDTO))
    );

    result.andExpect(MockMvcResultMatchers.status().isCreated());

    List<Position> positionsInDatabase = positionRepository.findAll();

    assertThat(positionsInDatabase).hasSize(1);
  }

  @Test
  public void update_graduate_position_success() throws Exception {
    authenticateUser();

    Position positionInDatabase = Position.builder()
      .graduate(graduate)
      .location("Location")
      .description("Description")
      .startYear(2005)
      .build();

    positionInDatabase = positionRepository.save(positionInDatabase);

    UpdatePositionDTO positionDTO = UpdatePositionDTO.builder()
      .location("New Location")
      .description("New Description")
      .startYear(2000)
      .endYear(2004)
      .build();

    var result = mockMvc.perform(
      MockMvcRequestBuilders.put("/graduate/position/{id}", positionInDatabase.getId())
      .cookie(cookie)
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(positionDTO))
    );

    // Test
    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    positionInDatabase = positionRepository.findById(positionInDatabase.getId()).orElse(null);

    assertThat(positionInDatabase.getDescription()).isEqualTo(positionDTO.getDescription());
    assertThat(positionInDatabase.getLocation()).isEqualTo(positionDTO.getLocation());
    assertThat(positionInDatabase.getStartYear()).isEqualTo(positionDTO.getStartYear());
    assertThat(positionInDatabase.getEndYear()).isEqualTo(positionDTO.getEndYear());
    
  }

  @Test
  public void delete_graduate_position_success() throws Exception {
    authenticateUser();

    Position positionInDatabase = Position.builder()
      .graduate(graduate)
      .location("Location")
      .description("Description")
      .startYear(2005)
      .build();

    positionInDatabase = positionRepository.save(positionInDatabase);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.delete("/graduate/position/{id}", positionInDatabase.getId())
      .cookie(cookie)
    );

    // Test
    result.andExpect(MockMvcResultMatchers.status().isNoContent());

    Optional<Position> databaseResponse = positionRepository.findById(positionInDatabase.getId());

    assertThat(databaseResponse.isEmpty()).isTrue();
  }
}
