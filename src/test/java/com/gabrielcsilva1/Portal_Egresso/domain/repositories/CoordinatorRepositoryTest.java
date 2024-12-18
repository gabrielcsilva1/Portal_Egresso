package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Coordinator;

@DataJpaTest
@ActiveProfiles("test")
public class CoordinatorRepositoryTest {
  @Autowired
  private CoordinatorRepository coordinatorRepository;

  @Test
  @DisplayName("should be able to register a coordinator")
  public void register_coordinator() {
    Coordinator coordinator = Coordinator.builder()
      .login("johndoe")
      .password("123456")
      .build();

    var result = this.coordinatorRepository.save(coordinator);

    assertNotNull(result);
    assertNotNull(result.getId());
  }

  @Test
  @DisplayName("should be able to find a coordinator by login")
  public void find_coordinator_by_login() {
    Coordinator coordinator = Coordinator.builder()
      .login("johndoe")
      .password("123456")
      .build();
    
    this.coordinatorRepository.save(coordinator);
    
    Optional<Coordinator> result = this.coordinatorRepository.findByLogin(coordinator.getLogin());

    assertTrue(result.isPresent());
  }
}
