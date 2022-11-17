package com.martynenko.anton.company.project;

import java.time.LocalDate;
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
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class Project {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, nullable = false)
  private String title;
  @Column(nullable = false)
  private LocalDate startDate;


  private LocalDate endDate;

  public Project(ProjectDTO projectDTO) {
    this.title = projectDTO.title();
    this.startDate = projectDTO.startDate();
    this.endDate = projectDTO.endDate();
  }

  public ProjectDTO toDTO() {
    return new ProjectDTO(this.id,
        this.title,
        this.startDate,
        this.endDate);
  }

  public Project update(ProjectDTO projectDTO) {
    this.title = projectDTO.title();
    this.startDate = projectDTO.startDate();
    this.endDate = projectDTO.endDate();
    return this;
  }
}
