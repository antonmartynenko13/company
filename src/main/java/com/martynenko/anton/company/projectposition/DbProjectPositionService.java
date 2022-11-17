package com.martynenko.anton.company.projectposition;

import com.martynenko.anton.company.project.Project;
import com.martynenko.anton.company.project.ProjectService;
import com.martynenko.anton.company.user.User;
import com.martynenko.anton.company.user.UserService;
import java.util.Collection;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DbProjectPositionService implements ProjectPositionService{
  private final ProjectPositionRepository projectPositionRepository;
  
  private final UserService userService;
  
  private final ProjectService projectService;

  @Autowired
  public DbProjectPositionService(ProjectPositionRepository projectPositionRepository,
      UserService userService, ProjectService projectService) {
    this.projectPositionRepository = projectPositionRepository;
    this.userService = userService;
    this.projectService = projectService;
  }

  @Override
  public ProjectPosition create(ProjectPositionDTO newProjectPosition) {
    User user = userService.get(newProjectPosition.userId());
    Project project = projectService.get(newProjectPosition.projectId());

    ProjectPosition projectPosition = newProjectPosition.createInstance(user, project);
    return projectPositionRepository.save(projectPosition);
  }

  @Override
  public ProjectPosition update(Long id, ProjectPositionDTO updated) {
    User user = userService.get(updated.userId());
    Project project = projectService.get(updated.projectId());

    ProjectPosition projectPosition = get(id);
    return projectPositionRepository.save(projectPosition.update(updated, user, project));
  }

  @Override
  public ProjectPosition get(Long id) {
    return projectPositionRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(id + "")
    );
  }

  @Override
  public Collection<ProjectPosition> listAll() {
    return projectPositionRepository.findAll();
  }

  @Override
  public void delete(Long id) {
    get(id);
    projectPositionRepository.deleteById(id);
  }
}
