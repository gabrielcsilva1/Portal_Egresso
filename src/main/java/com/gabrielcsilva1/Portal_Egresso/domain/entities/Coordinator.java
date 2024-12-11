package com.gabrielcsilva1.Portal_Egresso.domain.entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "coordinator")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Coordinator {
  @Id
  @GeneratedValue( strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String login;

  @Column(nullable = false)
  private String password;
}
