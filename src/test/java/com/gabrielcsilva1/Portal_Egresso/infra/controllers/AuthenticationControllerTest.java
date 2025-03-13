package com.gabrielcsilva1.Portal_Egresso.infra.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;
import com.gabrielcsilva1.Portal_Egresso.domain.repositories.CoordinatorRepository;
import com.gabrielcsilva1.Portal_Egresso.dtos.request.authentication.RequestCoordinatorAuthenticationJson;
import com.gabrielcsilva1.utils.faker.FakeCoordinatorFactory;

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

  @Test
  public void authentication_success() throws Exception {
    Coordinator coordinator = FakeCoordinatorFactory.makeCoordinator();
    String rawPassword = coordinator.getPassword();

    coordinator.setPassword(passwordEncoder.encode(rawPassword));
    coordinatorRepository.save(coordinator);

    RequestCoordinatorAuthenticationJson authenticationDTO = new RequestCoordinatorAuthenticationJson(coordinator.getLogin(), rawPassword);

    String requestJson = objectMapper.writeValueAsString(authenticationDTO);

    var result = mockMvc.perform(
      MockMvcRequestBuilders.post("/coordinator/session")
        .contentType(MediaType.APPLICATION_JSON)
        .content(requestJson)
    );

    result.andExpect(MockMvcResultMatchers.status().isOk());

    String setCookieHeader  = result.andReturn().getResponse().getHeader("Set-Cookie");
    assertThat(setCookieHeader).isNotNull();
    if (setCookieHeader != null) {
      assertTrue(setCookieHeader.contains("jwtToken="));
      assertTrue(setCookieHeader.contains("HttpOnly"));
    }
  }
}
