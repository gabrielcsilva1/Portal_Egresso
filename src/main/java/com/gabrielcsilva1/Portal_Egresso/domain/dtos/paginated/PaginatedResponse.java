package com.gabrielcsilva1.Portal_Egresso.domain.dtos.paginated;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginatedResponse<T> {
  private List<T> content;
  private int pageNumber;
  private int totalPages;
  private long totalElements;
}
