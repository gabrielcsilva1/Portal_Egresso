package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;

@DataJpaTest
@ActiveProfiles("test")
public class EgressRepositoryTest {
  @Autowired
  private EgressRepository egressRepository;

  @Test
  @DisplayName("should be able to create a new egress")
  public void save_new_egress() {
    Egress egress = Egress.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .build();

    Egress result = this.egressRepository.save(egress);

    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals(result.getName(), egress.getName());
  }

  @Test
  @DisplayName("should be able to get a egress by id")
  public void get_egress_by_id() {
    Egress egress = Egress.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .build();

    Egress savedEgress = this.egressRepository.save(egress);

    Optional<Egress> result = this.egressRepository.findById(savedEgress.getId());

    assertTrue(result.isPresent());
  }
}
