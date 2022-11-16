package com.martynenko.anton.company.project;

import java.util.Collection;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DbProjectService implements ProjectService{

  private final ProjectRepository projectRepository;

  @Autowired
  public DbProjectService(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  @Override
  public Project create(ProjectDTO newProject) {
    return projectRepository.save(new Project(newProject));
  }

  @Override
  public Project update(ProjectDTO updated) {
    return projectRepository.save(get(updated.id()).update(updated));
  }

  @Override
  public Project get(Long id) {
    return projectRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(id + "")
    );
  }

  @Override
  public Collection<Project> listAll() {
    return projectRepository.findAll();
  }

  @Override
  public void delete(Long id) {
    get(id);
    projectRepository.deleteById(id);
  }
}
