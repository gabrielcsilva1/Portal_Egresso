package com.gabrielcsilva1.Portal_Egresso.domain.entities;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "course")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Course {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "coordinator_id", nullable = false)
  private Coordinator coordinator;

  @OneToMany(mappedBy = "course")
  private Set<EgressCourse> egressCourse;

  @Column(nullable = false)
  @NotBlank
  private String name;

  @Column(nullable = false)
  @NotBlank
  private String level;
}
