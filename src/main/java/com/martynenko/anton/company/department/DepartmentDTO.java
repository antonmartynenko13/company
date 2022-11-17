package com.martynenko.anton.company.department;

import java.util.Objects;

public record DepartmentDTO (Long id, String title){
  public DepartmentDTO {
    Objects.requireNonNull(title);
  }
  public Department createInstance() {
    return new Department().update(this);
  }
}
