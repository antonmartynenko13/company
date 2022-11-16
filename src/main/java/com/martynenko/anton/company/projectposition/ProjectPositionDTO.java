package com.martynenko.anton.company.projectposition;

import java.time.LocalDate;
import java.util.Objects;

public record ProjectPositionDTO(Long id,
                                 Long userId,
                                 Long projectId,
                                 LocalDate positionStartDate,
                                 LocalDate positionEndDate,
                                 String positionTitle,
                                 String occupation) {
  public ProjectPositionDTO {
    Objects.requireNonNull(userId);
    Objects.requireNonNull(projectId);
    Objects.requireNonNull(positionStartDate);
    Objects.requireNonNull(positionEndDate);
    Objects.requireNonNull(positionTitle);
    Objects.requireNonNull(occupation);
  }
}
