package org.itsci.attendance.service;

import org.itsci.attendance.model.Authority;
import org.itsci.attendance.model.User;

import java.util.List;

public interface UserService {

    User getUser(Long id);

    User findByUsername(String username);

    void saveUser(User user);

    List<User> getUsers();

    void deleteUser(Long id);

    void updateUser(User user, List<Authority> authorityToRemove, List<Authority> authorityToAdd);

    void register(User user);
}
