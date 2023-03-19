package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.UserRepository;
import org.foxminded.rymarovych.model.User;
import org.foxminded.rymarovych.service.abstractions.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAllUsersList() {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public User add(User user) {

        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);

        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User user) {

        Optional<User> optionalCurrentUser = userRepository.findById(id);

        if (optionalCurrentUser.isPresent()) {
            User currentUser = optionalCurrentUser.get();

            currentUser.setUsername(user.getUsername());
            currentUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            currentUser.setRoles(user.getRoles());
            currentUser.setEnabled(user.isEnabled());

            return userRepository.save(currentUser);

        } else {
            return new User();
        }
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }


}
