package org.example.taskflow.core.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserDTO {

    private Long id;
    private String name;
    private String surname;
    private String username;
    private String email;
    private Integer jetons;
    private RoleDTO role;
    private List<JetonUsageDTO> jetonUsages;
}