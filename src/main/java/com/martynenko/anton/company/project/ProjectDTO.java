package com.martynenko.anton.company.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.martynenko.anton.company.department.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Objects;

@Schema(name = "Project")
public record ProjectDTO(
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long id,
    @Schema(required = true, description = "Unique")
    String title,

    @Schema(required = true)
    LocalDate startDate,
    LocalDate endDate
) {

  public ProjectDTO {
    Objects.requireNonNull(title);
    Objects.requireNonNull(startDate);
  }

  public Project createInstance() {
    return new Project().update(this);
  }
}
