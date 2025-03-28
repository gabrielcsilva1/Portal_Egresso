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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "position")
public class Position {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "graduate_id", nullable = false)
  private Graduate graduate;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String location;

  @Column(name = "start_year", nullable = false)
  private Integer startYear;

  @Column(name = "end_year")
  private Integer endYear;
}
