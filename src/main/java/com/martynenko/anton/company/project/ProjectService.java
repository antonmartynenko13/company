package com.martynenko.anton.company.project;

import com.martynenko.anton.company.user.User;
import com.martynenko.anton.company.user.UserDTO;
import java.util.Collection;

public interface ProjectService {

  Project create(ProjectDTO newProject);

  Project update(ProjectDTO updated);

  Project get(Long id);
  Collection<Project> listAll();

  void delete(Long id);
}
