package com.martynenko.anton.company.department;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@EqualsAndHashCode
@NoArgsConstructor
@Getter
@ToString
public class Department {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String title;

  public Department(DepartmentDTO departmentDTO) {
    this.title = departmentDTO.title();
  }

  Department update(DepartmentDTO departmentDTO) {
    this.title = departmentDTO.title();
    return this;
  }

  DepartmentDTO toDTO() {
    return new DepartmentDTO(id, title);
  }
}
