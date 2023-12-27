package org.example.taskflow.core.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDTO {

    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private Integer jetons;
    private RoleDTO role;
    private List<TaskDTO> tasks;
    private List<JetonUsageDTO> jetonUsages;
}