package com.martynenko.anton.company.user;

import com.martynenko.anton.company.department.Department;
import com.martynenko.anton.company.department.DepartmentService;
import java.util.Collection;
import javax.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Service
@Primary
public class DbUserService implements UserService{

  private final UserRepository userRepository;

  private final DepartmentService departmentService;

  @Autowired
  public DbUserService(UserRepository userRepository, DepartmentService departmentService) {
    this.userRepository = userRepository;
    this.departmentService = departmentService;
  }

  @Override
  public User create(UserDTO newUser) {
    Department department = departmentService.get(newUser.departmentId());
    User user = new User(newUser, department);
    return userRepository.save(user);
  }

  @Override
  public User update(UserDTO updated) {
    Department department = departmentService.get(updated.departmentId());
    User user = get(updated.id());
    return userRepository.save(user.update(updated, department));
  }

  @Override
  public User get(Long id) {
    return userRepository.findById(id).orElseThrow(
        () -> new EntityNotFoundException(id + "")
    );
  }

  @Override
  public Collection<User> listAll() {
    return userRepository.findAll();
  }


  @Override
  public void delete(Long id) {
    get(id);
    userRepository.deleteById(id);
  }
}
