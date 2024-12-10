package com.gabrielcsilva1.Portal_Egresso.domain.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Entity
@Table(name = "position")
public class Position {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "egress_id", nullable = false)
  private Egress egress;

  @NotBlank
  @Column(nullable = false)
  private String description;

  @NotBlank
  @Column(nullable = false)
  private String company;

  @Positive
  @Column(name = "start_year", nullable = false)
  private Integer startYear;

  @Positive
  @Column(name = "end_year")
  private Integer endYear;
}
