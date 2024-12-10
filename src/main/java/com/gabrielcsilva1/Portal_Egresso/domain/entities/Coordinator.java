package com.gabrielcsilva1.Portal_Egresso.domain.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Entity
@Table(name = "coordinator")
@Builder
@Getter
public class Coordinator {
  @Id
  @GeneratedValue( strategy = GenerationType.UUID)
  private UUID id;

  @NotBlank
  @Column(nullable = false, unique = true)
  private String login;

  @NotBlank
  @Column(nullable = false)
  private String password;
}
