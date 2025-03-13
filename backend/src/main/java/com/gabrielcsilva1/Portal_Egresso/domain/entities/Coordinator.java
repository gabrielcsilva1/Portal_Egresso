package com.gabrielcsilva1.Portal_Egresso.domain.entities;

import java.util.List;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.dtos.enums.RoleEnum;

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
import lombok.Setter;

@Entity
@Table(name = "coordinator")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Coordinator implements IGenericUser {
  @Id
  @GeneratedValue( strategy = GenerationType.UUID)
  private UUID id;

  @Column(nullable = false, unique = true)
  private String login;

  @Column(nullable = false)
  private String password;

  @Override
  public List<RoleEnum> getRoles() {
    return List.of(RoleEnum.COORDINATOR);
  }
}
