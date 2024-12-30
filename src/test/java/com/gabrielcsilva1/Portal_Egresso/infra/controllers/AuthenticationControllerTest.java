package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import static org.assertj.core.api.Assertions.assertThat;

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
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.AuthenticationDTO;
import com.gabrielcsilva1.Portal_Egresso.domain.dtos.authentication.AuthenticationResponse;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class AuthenticationControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;
  
  @Autowired
  private ObjectMapper objectMapper;

  private Coordinator coordinator;

  @BeforeEach
  public void setUp() {
    Coordinator coordinatorToSave = Coordinator.builder()
      .login("admin")
      .password(passwordEncoder.encode("admin"))
      .build();

    this.coordinator = coordinatorRepository.save(coordinatorToSave);
  }

  @Test
  public void authentication_success() throws Exception {
    AuthenticationDTO authenticationDTO = new AuthenticationDTO(coordinator.getLogin(), "admin");

    String requestJson = objectMapper.writeValueAsString(authenticationDTO);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.post("/coordinator/session")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson)
    );

    result.andExpect(MockMvcResultMatchers.status().isOk());

    String responseJson = result.andReturn().getResponse().getContentAsString();
    AuthenticationResponse loginResponse = objectMapper.readValue(responseJson, AuthenticationResponse.class);

    assertThat(loginResponse).isNotNull();
    assertThat(loginResponse.token()).isNotBlank();
  }
}
