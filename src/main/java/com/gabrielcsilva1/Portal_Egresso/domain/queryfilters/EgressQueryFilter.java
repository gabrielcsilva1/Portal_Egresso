package com.gabrielcsilva1.Portal_Egresso.domain.queryfilters;

import static com.gabrielcsilva1.Portal_Egresso.domain.specifications.EgressFilterSpecification.*;

import org.springframework.data.jpa.domain.Specification;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EgressQueryFilter {
  private String name;
  private String courseName;
  private Integer year;

  public Specification<Egress> toSpecification() {
    return isNameEqualTo(name)
      .and(didEgressTakenCourseInTheYear(year))
      .and(didEgressTakenTheCourseByName(courseName));
  }
}
