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
@Table(name = "testimonial")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Testimonial {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "graduate_id", nullable = false)
  private Graduate graduate;

  private String text;

  @Column(name = "created_at", nullable = false)
  private LocalDateTime createdAt;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private StatusEnum registrationStatus;

  @PrePersist
  private void prePersist() {
    if (this.registrationStatus == null) {
      this.registrationStatus = StatusEnum.PENDING;
    }
    if (this.createdAt == null){
      this.createdAt = LocalDateTime.now();
    }
  }
}
