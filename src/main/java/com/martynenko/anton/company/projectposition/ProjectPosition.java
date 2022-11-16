package com.martynenko.anton.company.projectposition;

import com.martynenko.anton.company.project.Project;
import com.martynenko.anton.company.user.User;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class ProjectPosition {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "project_id")
  private Project project;

  @Column(name = "position_start_date")
  private LocalDate positionStartDate;

  @Column(name = "position_end_date")
  private LocalDate positionEndDate;

  @Column(name = "position_title")
  private String positionTitle;

  private String occupation;

  public ProjectPosition(ProjectPositionDTO projectPositionDTO, User user, Project project) {
    this.user = user;
    this.project = project;
    this.positionStartDate = projectPositionDTO.positionStartDate();
    this.positionEndDate = projectPositionDTO.positionEndDate();
    this.positionTitle = projectPositionDTO.positionTitle();
    this.occupation = projectPositionDTO.occupation();
  }

  public ProjectPositionDTO toDTO() {
    return new ProjectPositionDTO(this.id,
        this.user.getId(),
        this.project.getId(),
        this.positionStartDate,
        this.positionEndDate,
        this.positionTitle,
        this.occupation);
  }

  public ProjectPosition update(ProjectPositionDTO projectPositionDTO, User user, Project project) {
    this.user = user;
    this.project = project;
    this.positionStartDate = projectPositionDTO.positionStartDate();
    this.positionEndDate = projectPositionDTO.positionEndDate();
    this.positionTitle = projectPositionDTO.positionTitle();
    this.occupation = projectPositionDTO.occupation();
    return this;
  }
}
