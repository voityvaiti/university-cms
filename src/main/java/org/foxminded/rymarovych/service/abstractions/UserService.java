package org.foxminded.rymarovych.service.abstractions;

import org.foxminded.rymarovych.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAllUsersList();

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    User add(User user);

    User update(Long id, User user);

    void delete(Long id);
}
