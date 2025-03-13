package com.gabrielcsilva1.Portal_Egresso.domain.entities;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.dtos.enums.RoleEnum;
import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
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
public class Graduate implements IGenericUser{
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @Builder.Default
  @OneToMany(mappedBy = "graduate", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
  private Set<GraduateCourse> graduateCourses = new HashSet<>();

  @Builder.Default
  @OneToMany(mappedBy = "graduate", cascade = CascadeType.REMOVE)
  private Set<Position> positions = new HashSet<>();

  @OneToMany(mappedBy = "graduate", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private Set<Testimonial> testimonials;

  @OneToMany(mappedBy = "graduate", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
  private Set<Testimonial> jobOpportunities;
  
  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String password;

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

  @Column(nullable = false, name = "registration_status")
  @Enumerated(EnumType.STRING)
  private StatusEnum registrationStatus;

  @PrePersist
  public void prePersist() {
    if (this.registrationStatus == null) {
      this.registrationStatus = StatusEnum.PENDING;
    }
  }

  @Override
  public List<RoleEnum> getRoles() {
    if (this.registrationStatus == StatusEnum.ACCEPTED) {
      return List.of(RoleEnum.GRADUATE);
    }

    return Collections.emptyList();
  }
}
