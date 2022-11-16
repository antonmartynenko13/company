package com.martynenko.anton.company.project;

import java.time.LocalDate;
import java.util.Objects;

public record ProjectDTO(Long id, String title, LocalDate startDate, LocalDate endDate) {

  public ProjectDTO {
    Objects.requireNonNull(title);
    Objects.requireNonNull(startDate);
    Objects.requireNonNull(endDate);
  }
}
