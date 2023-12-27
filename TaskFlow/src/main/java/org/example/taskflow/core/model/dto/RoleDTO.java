package org.example.taskflow.core.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class RoleDTO {

    private Long id;
    private String name;
    private List<UserDTO> users;
}