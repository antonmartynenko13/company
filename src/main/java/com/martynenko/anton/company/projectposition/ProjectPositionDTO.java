package com.martynenko.anton.company.projectposition;

import com.martynenko.anton.company.project.Project;
import com.martynenko.anton.company.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.Objects;

@Schema(name = "Project position")
public record ProjectPositionDTO( @Schema(accessMode = Schema.AccessMode.READ_ONLY)
                                  Long id,
                                 @Schema(required = true, description = "Present users id")
                                 Long userId,
                                 @Schema(required = true, description = "Present projects id")
                                 Long projectId,
                                 @Schema(required = true)
                                 LocalDate positionStartDate,
                                 LocalDate positionEndDate,
                                 @Schema(required = true)
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
