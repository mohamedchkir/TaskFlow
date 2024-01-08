package org.example.taskflow.core.mapper;

import org.example.taskflow.core.model.dto.RoleDTO;
import org.example.taskflow.core.model.dto.TagDTO;
import org.example.taskflow.core.model.entity.Role;
import org.example.taskflow.core.model.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleMapper INSTANCE = Mappers.getMapper(RoleMapper.class);
    RoleDTO roleToRoleDTO(Role role);

    Role roleDTOToRole(RoleDTO roleDTO);
}
