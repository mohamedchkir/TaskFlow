package org.example.taskflow.core.service;

import org.example.taskflow.core.model.dto.RoleDTO;
import org.example.taskflow.core.model.dto.TagDTO;
import org.example.taskflow.core.model.entity.Role;

import java.util.List;

public interface RoleService {
    RoleDTO getRoleById(Long roleId);

    RoleDTO getRoleByName(String roleName);

    List<RoleDTO> getAllRoles();

    RoleDTO createRole(RoleDTO roleDTO);

    RoleDTO updateRole(RoleDTO roleDTO);

    void deleteRole(Long roleId);
}
