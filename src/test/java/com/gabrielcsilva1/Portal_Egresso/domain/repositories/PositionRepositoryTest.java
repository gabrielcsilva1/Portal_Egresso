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

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Graduate;
import com.gabrielcsilva1.Portal_Egresso.domain.entities.Position;

@DataJpaTest
@ActiveProfiles("test")
public class PositionRepositoryTest {
  @Autowired
  private PositionRepository positionRepository;

  @Autowired
  private GraduateRepository graduateRepository;

  private Graduate graduate;

  @BeforeEach
  public void setUp() {
    Graduate graduate = Graduate.builder()
      .name("john doe")
      .email("johndoe@example.com")
      .build();

    this.graduate = this.graduateRepository.save(graduate);
  }

  @Test
  @DisplayName("should be able to register a graduate position")
  public void register_graduate_position() {
    Position position = Position.builder()
      .graduate(graduate)
      .description("Software Engineer")
      .location("XYZ Corp.")
      .startYear(2000)
      .build();

    Position result = this.positionRepository.save(position);

    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals(result.getGraduate().getId(), graduate.getId());
  }

  @Test
  @DisplayName("should be able to register a graduate position")
  public void get_graduate_position_by_id() {
    Position position = Position.builder()
      .graduate(graduate)
      .description("Software Engineer")
      .location("XYZ Corp.")
      .startYear(2000)
      .build();

    Position savedPosition = this.positionRepository.save(position);

    Optional<Position> result = this.positionRepository.findById(savedPosition.getId());

    assertTrue(result.isPresent());
  }
}
