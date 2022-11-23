package com.martynenko.anton.company.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.martynenko.anton.company.department.Department;
import java.util.Objects;

public record UserDTO(Long id,
                      @JsonProperty("firstName") String firstName,
                      @JsonProperty("lastName") String lastName,
                      @JsonProperty("email") String email,
                      @JsonProperty("jobTitle") String jobTitle,
                      @JsonProperty("departmentId") Long departmentId) {

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
