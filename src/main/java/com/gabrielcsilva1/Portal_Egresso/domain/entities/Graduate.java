package com.gabrielcsilva1.Portal_Egresso.domain.entities;

import java.util.Set;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.domain.dtos.GraduateDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "graduate")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Graduate {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @OneToMany(mappedBy = "graduate")
  private Set<GraduateCourse> graduateCourses;

  @OneToMany(mappedBy = "graduate")
  private Set<Position> positions;
  
  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
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

  public Graduate(GraduateDTO graduate) {
    this.name = graduate.getName();
    this.email = graduate.getEmail();
    this.description = graduate.getDescription();
    this.avatarUrl = graduate.getAvatarUrl();
    this.linkedin = graduate.getLinkedin();
    this.instagram = graduate.getInstagram();
    this.curriculum = graduate.getCurriculum();
  }
}