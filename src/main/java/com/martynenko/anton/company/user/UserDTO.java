package com.martynenko.anton.company.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.martynenko.anton.company.department.Department;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
@Schema(name = "User")
public record UserDTO(
  @Schema(accessMode = Schema.AccessMode.READ_ONLY)
  Long id,
  @Schema(required = true)
  @JsonProperty("firstName") String firstName,
  @Schema(required = true)
  @JsonProperty("lastName") String lastName,
  @Schema(required = true, description = "Unique")
  @JsonProperty("email") String email,
  @Schema(required = true)
  @JsonProperty("jobTitle") String jobTitle,
  @Schema(required = true, description = "Present department id")
  @JsonProperty("departmentId") Long departmentId
) {

  public UserDTO {
    Objects.requireNonNull(firstName);
    Objects.requireNonNull(lastName);
    Objects.requireNonNull(email);
    Objects.requireNonNull(jobTitle);
    Objects.requireNonNull(departmentId);
  }

  public User createInstance(Department department) {
    return new User().update(this, department);
  }
}
