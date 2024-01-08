package org.example.taskflow.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.example.taskflow.core.mapper.RoleMapper;
import org.example.taskflow.core.model.dto.RoleDTO;
import org.example.taskflow.core.model.entity.Role;
import org.example.taskflow.core.repository.RoleRepository;
import org.example.taskflow.core.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    @Override
    public RoleDTO getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }
        return RoleMapper.INSTANCE.roleToRoleDTO(role);
    }

    @Override
    public RoleDTO getRoleByName(String roleName) {

        Role role = roleRepository.findByName(roleName).orElse(null);
        if (role == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }
        return RoleMapper.INSTANCE.roleToRoleDTO(role);
    }

    @Override
    public List<RoleDTO> getAllRoles() {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Roles not found");
        }
        return roles
                .stream()
                .map(RoleMapper.INSTANCE::roleToRoleDTO)
                .collect(Collectors.toList());
    }

    @Override
    public RoleDTO createRole(RoleDTO roleDTO) {
        if (roleRepository.findByName(roleDTO.getName()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role already exists");
        }
        Role role = RoleMapper.INSTANCE.roleDTOToRole(roleDTO);
        Role savedRole = roleRepository.save(role);
        return RoleMapper.INSTANCE.roleToRoleDTO(savedRole);
    }

    @Override
    public RoleDTO updateRole(RoleDTO roleDTO) {
        Role role = roleRepository.findById(roleDTO.getId()).orElse(null);
        if (role == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }
        role.setName(roleDTO.getName());
        if (roleRepository.findByName(role.getName()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role already exists");
        }
        Role savedRole = roleRepository.save(role);
        return RoleMapper.INSTANCE.roleToRoleDTO(savedRole);

    }

    @Override
    public void deleteRole(Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if (role == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found");
        }
        roleRepository.delete(role);
        throw new ResponseStatusException(HttpStatus.OK, "Role deleted");

    }
}
