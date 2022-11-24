package com.martynenko.anton.company.user;

import com.martynenko.anton.company.department.Department;
import com.martynenko.anton.company.department.DepartmentService;
import java.time.LocalDate;
import java.util.Collection;
import javax.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
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
    User user = newUser.createInstance(department);
    return userRepository.save(user);
  }

  @Override
  public User update(Long id, UserDTO updated) {
    Department department = departmentService.get(updated.departmentId());
    User user = get(id);
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

  @Override
  public Collection<User> listAvailable(long period) {
    LocalDate current = LocalDate.now();
    return userRepository.findAvailable(current, current.plusDays(period));
  }

  @Override
  //to prevent duplication errors file shouldn't be imported partly if exception is thrown during iteration
  @Transactional
  public void create(Collection<UserDTO> newUsers) {
    newUsers.forEach(this::create);
  }
}
