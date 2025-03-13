package com.gabrielcsilva1.Portal_Egresso.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import com.gabrielcsilva1.Portal_Egresso.dtos.enums.StatusEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "job_opportunity")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class JobOpportunity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "graduate_id", nullable = false)
  private Graduate graduate;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false, length = 1000)
  private String description;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private StatusEnum registrationStatus;

  @Column(nullable = false, name = "created_at")
  private LocalDateTime createdAt;

  @PrePersist
  private void prePersist() {
    if (this.registrationStatus == null) {
      this.registrationStatus = StatusEnum.PENDING;
    }
    
    if (this.createdAt == null) {
      this.createdAt = LocalDateTime.now();
    }
  }
}
