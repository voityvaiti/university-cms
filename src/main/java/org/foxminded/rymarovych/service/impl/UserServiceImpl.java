package org.foxminded.rymarovych.service.impl;

import org.foxminded.rymarovych.dao.UserRepository;
import org.foxminded.rymarovych.model.User;
import org.foxminded.rymarovych.service.abstractions.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> getAllUsersList() {
        LOGGER.debug("Returning all users list");

        return userRepository.findAll();
    }

    @Override
    public Optional<User> findById(Long id) {
        LOGGER.debug("Returning Optional User, found by ID: {}", id);

        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        LOGGER.debug("Returning Optional User, found by Username: {}", username);

        return userRepository.findByUsername(username);
    }

    @Override
    public User add(User user) {
        LOGGER.debug("Received User for addition: {}", user);

        String encodedPassword = new BCryptPasswordEncoder().encode(user.getPassword());
        user.setPassword(encodedPassword);

        LOGGER.debug("Password of received User encoded. Saving new User: {}", user);

        return userRepository.save(user);
    }

    @Override
    public User update(Long id, User user) {

        LOGGER.debug("Received User to update: {} by ID: {}", user, id);

        Optional<User> optionalCurrentUser = userRepository.findById(id);

        if (optionalCurrentUser.isPresent()) {
            User currentUser = optionalCurrentUser.get();

            LOGGER.debug("Found User to update: {} by ID: {}", currentUser, id);

            currentUser.setUsername(user.getUsername());
            currentUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
            currentUser.setRoles(user.getRoles());
            currentUser.setEnabled(user.isEnabled());

            LOGGER.debug("Saving updated User: {}", currentUser);

            return userRepository.save(currentUser);

        } else {
            LOGGER.warn("Not found User to update by ID: {}", id);

            return new User();
        }
    }

    @Override
    public void delete(Long id) {
        LOGGER.info("Deleting user by ID: {}", id);

        userRepository.deleteById(id);
    }


}
