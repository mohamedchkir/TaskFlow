package org.example.taskflow.core.service;

import org.example.taskflow.core.model.dto.UserDTO;

import java.util.List;

public interface UserService {

    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long userId);
}
