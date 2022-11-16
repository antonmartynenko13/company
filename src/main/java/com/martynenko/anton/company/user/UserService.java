package com.martynenko.anton.company.user;

import java.util.Collection;

public interface UserService {

  User create(UserDTO newUser);

  User update(UserDTO updated);

  User get(Long id);
  Collection<User> listAll();

  void delete(Long id);
}
