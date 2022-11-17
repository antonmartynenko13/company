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
    return projectRepository.save(newProject.createInstance());
  }

  @Override
  public Project update(Long id, ProjectDTO updated) {
    return projectRepository.save(get(id).update(updated));
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
