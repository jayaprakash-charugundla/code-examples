package com.jc.ac.service;

import com.jc.ac.domain.Role;
import com.jc.ac.domain.User;
import java.util.List;

public interface UserService {
    User saveUser(User user);
    Role saveRole(Role role);
    void addRoleToUser(String username, String roleName);
    User getUser(String username);
    List<User> getUsers();
}
