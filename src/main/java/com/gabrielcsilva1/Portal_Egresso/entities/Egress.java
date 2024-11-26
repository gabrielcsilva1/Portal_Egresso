package com.gabrielcsilva1.Portal_Egresso.entities;

import java.util.Set;
import java.util.UUID;

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
  private Set<CourseEgress> courseEgress;
  
  @Column(nullable = false)
  @NotBlank
  private String name;

  @Column(nullable = false)
  @NotBlank
  @Email
  private String email;

  private String description;

  @Column(name = "avatar_url")
  private String avatarUrl;

  @Column(name = "linkedin_url")
  private String linkedinUrl;

  @Column(name = "instagram_url")
  private String instagramUrl;

  @Column(name = "curriculum_url")
  private String curriculumUrl;
}
