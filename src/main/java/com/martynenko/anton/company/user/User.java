package com.martynenko.anton.company.user;

import com.martynenko.anton.company.department.Department;
import com.martynenko.anton.company.projectposition.ProjectPosition;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "users") //user is reserved word
@Getter
@EqualsAndHashCode
@NoArgsConstructor
@ToString
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "job_title", nullable = false)
  private String jobTitle;

  @ManyToOne
  @JoinColumn(name = "department_id")
  private Department department;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  private ProjectPosition projectPosition;

  public User(UserDTO userDTO, Department department) {
    this.firstName = userDTO.firstName();
    this.lastName = userDTO.lastName();
    this.email = userDTO.email();
    this.jobTitle = userDTO.jobTitle();
    this.department = department;
  }

  public UserDTO toDTO() {
    return new UserDTO(this.id,
        this.firstName,
        this.lastName,
        this.email,
        this.jobTitle,
        this.department.getId());
  }

  public User update(UserDTO userDTO, Department department) {
    this.firstName = userDTO.firstName();
    this.lastName = userDTO.lastName();
    this.email = userDTO.email();
    this.jobTitle = userDTO.jobTitle();
    this.department = department;
    return this;
  }

  public String getFullName() {
    return String.format("%s %s", this.getFirstName(), this.getLastName());
  }

}
