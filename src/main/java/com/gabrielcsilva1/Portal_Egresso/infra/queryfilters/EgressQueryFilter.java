package com.gabrielcsilva1.Portal_Egresso.infra.queryfilters;

import static com.gabrielcsilva1.Portal_Egresso.domain.specifications.EgressFilterSpecification.*;

import org.springframework.data.jpa.domain.Specification;

import com.gabrielcsilva1.Portal_Egresso.domain.entities.Egress;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EgressQueryFilter {
  /*
   * query - name or position name
   */
  private String query;
  private String courseName;
  private Integer year;

  public Specification<Egress> toSpecification() {
    var isNameOrPositionEqualToQuery = isNameEqualTo(query).or(didEgressHoldPositionNamed(query));

    return isNameOrPositionEqualToQuery
      .and(didEgressTakenCourseInTheYear(year))
      .and(didEgressTakenTheCourseByName(courseName));
  }
}
