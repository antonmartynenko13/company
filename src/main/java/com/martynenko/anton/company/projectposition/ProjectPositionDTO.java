package com.martynenko.anton.company.projectposition;

import com.martynenko.anton.company.project.Project;
import com.martynenko.anton.company.user.User;
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
    Objects.requireNonNull(positionTitle);
    Objects.requireNonNull(occupation);
  }

  public ProjectPosition createInstance(User user, Project project) {
    return new ProjectPosition().update(this, user, project);
  }
}
