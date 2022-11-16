package com.martynenko.anton.company.department;

import java.util.Collection;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Primary
public class DbDepartmentService implements DepartmentService{

  private final DepartmentRepository departmentRepository;

  @Override
  public Department create(DepartmentDTO newDepartment) {
    return departmentRepository.save(new Department(newDepartment));
  }

  @Override
  public Department update(DepartmentDTO updated) {
    return departmentRepository.save(get(updated.id()).update(updated));
  }

  @Override
  public Department get(Long id) {
    return departmentRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(id + "")
    );
  }
  @Override
  public Collection<Department> listAll() {
    return departmentRepository.findAll();
  }

  @Override
  public void delete(Long id) {
    get(id);
    departmentRepository.deleteById(id);
  }
}
