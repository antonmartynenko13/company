package com.martynenko.anton.company.project;

import com.martynenko.anton.company.department.Department;
import java.time.LocalDate;
import java.util.Objects;

public record ProjectDTO(Long id, String title, LocalDate startDate, LocalDate endDate) {

  public ProjectDTO {
    Objects.requireNonNull(title);
    Objects.requireNonNull(startDate);
  }

  public Project createInstance() {
    return new Project().update(this);
  }
}
