package com.gabrielcsilva1.Portal_Egresso.domain.entities;

import java.util.Set;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.EgressDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "egress")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Egress {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToMany(mappedBy = "egress")
  private Set<EgressCourse> egressCourse;
  
  @Column(nullable = false)
  @NotBlank
  private String name;

  @Column(nullable = false, unique = true)
  @NotBlank
  @Email
  private String email;

  private String description;

  @Column(name = "avatar_url")
  private String avatarUrl;

  @Column(name = "linkedin")
  private String linkedin;

  @Column(name = "instagram")
  private String instagram;

  @Column(name = "curriculum")
  private String curriculum;

  public Egress(EgressDTO egress) {
    this.name = egress.getName();
    this.email = egress.getEmail();
    this.description = egress.getDescription();
    this.avatarUrl = egress.getAvatarUrl();
    this.linkedin = egress.getLinkedin();
    this.instagram = egress.getInstagram();
    this.curriculum = egress.getCurriculum();
  }
}
