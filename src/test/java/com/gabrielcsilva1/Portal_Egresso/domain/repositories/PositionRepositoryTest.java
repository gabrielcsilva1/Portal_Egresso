package com.gabrielcsilva1.Portal_Egresso.domain.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;

@DataJpaTest
@ActiveProfiles("test")
public class PositionRepositoryTest {
  @Autowired
  private PositionRepository positionRepository;

  @Autowired
  private EgressRepository egressRepository;

  private Egress egress;

  @BeforeEach
  public void setUp() {
    Egress egress = Egress.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .build();

    this.egress = this.egressRepository.save(egress);
  }

  @Test
  @DisplayName("should be able to register a egress position")
  public void register_egress_position() {
    Position position = Position.builder()
      .egress(egress)
      .description("Software Engineer")
      .location("XYZ Corp.")
      .startYear(2000)
      .build();

    Position result = this.positionRepository.save(position);

    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals(result.getEgress().getId(), egress.getId());
  }

  @Test
  @DisplayName("should be able to register a egress position")
  public void get_egress_position_by_id() {
    Position position = Position.builder()
      .egress(egress)
      .description("Software Engineer")
      .location("XYZ Corp.")
      .startYear(2000)
      .build();

    Position savedPosition = this.positionRepository.save(position);

    Optional<Position> result = this.positionRepository.findById(savedPosition.getId());

    assertTrue(result.isPresent());
  }
}
