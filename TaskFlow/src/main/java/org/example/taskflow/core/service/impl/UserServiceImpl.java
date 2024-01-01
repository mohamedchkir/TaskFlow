package org.example.taskflow.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.taskflow.core.mapper.UserMapper;
import org.example.taskflow.core.model.dto.UserDTO;
import org.example.taskflow.core.model.entity.User;
import org.example.taskflow.core.repository.UserRepository;
import org.example.taskflow.core.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    @Override
    public List<UserDTO> getAllUsers() {
        return null;
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return userMapper.INSTANCE.userToUserDTO(user);
    }
}
