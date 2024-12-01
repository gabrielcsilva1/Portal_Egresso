package com.gabrielcsilva1.Portal_Egresso.domain.entities;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "testimonial")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Testimonial {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  @ManyToOne
  @JoinColumn(name = "egress_id", nullable = false)
  private Egress egress;

  private String text;

  @CreationTimestamp
  @Column(name = "created_at")
  private LocalDateTime createdAt;
}
