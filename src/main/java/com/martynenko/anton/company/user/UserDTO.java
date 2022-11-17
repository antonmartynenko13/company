package com.martynenko.anton.company.user;

import com.martynenko.anton.company.department.Department;
import com.martynenko.anton.company.project.Project;
import com.martynenko.anton.company.projectposition.ProjectPosition;
import java.io.Serializable;
import java.util.Objects;

public record UserDTO(Long id,
                      String firstName,
                      String lastName,
                      String email,
                      String password,
                      String jobTitle,
                      Long departmentId) {

  public UserDTO {
    Objects.requireNonNull(firstName);
    Objects.requireNonNull(lastName);
    Objects.requireNonNull(email);
    Objects.requireNonNull(password);
    Objects.requireNonNull(jobTitle);
    Objects.requireNonNull(departmentId);
  }

  public User createInstance(Department department) {
    return new User().update(this, department);
  }
}
