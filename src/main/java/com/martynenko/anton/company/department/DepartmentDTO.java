package com.martynenko.anton.company.department;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;

@Schema(name = "Department")
public record DepartmentDTO (
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @Schema(required = true, description = "Unique")
    String title
) {
  public DepartmentDTO {
    Objects.requireNonNull(title);
  }
  public Department createInstance() {
    return new Department().update(this);
  }
}
