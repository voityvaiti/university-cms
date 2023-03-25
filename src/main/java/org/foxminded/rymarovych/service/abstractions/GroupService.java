package org.foxminded.rymarovych.service.abstractions;

import org.foxminded.rymarovych.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupService {
    List<Group> getAllGroupsList();

    Optional<Group> findById(Long id);

    Group add(Group group);

    Group update(Long id, Group group);

    void delete(Long id);

}
