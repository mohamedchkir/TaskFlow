package org.example.taskflow.core.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.taskflow.core.model.dto.RoleDTO;
import org.example.taskflow.core.service.RoleService;
import org.example.taskflow.core.service.RoleService;
import org.example.taskflow.shared.Const.AppEndpoints;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(AppEndpoints.ROLE_ENDPOINT)
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> Roles = roleService.getAllRoles();
        return new ResponseEntity<>(Roles, HttpStatus.OK);
    }

    @GetMapping("/id/{RoleId}")
    public ResponseEntity<RoleDTO> getRoleById( @PathVariable Long RoleId) {
        RoleDTO Role = roleService.getRoleById(RoleId);
        if (Role != null) {
            return new ResponseEntity<>(Role, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/name/{RoleName}")
    public ResponseEntity<RoleDTO> getRoleByName(@PathVariable String RoleName) {
        RoleDTO Role = roleService.getRoleByName(RoleName);
        if (Role != null) {
            return new ResponseEntity<>(Role, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO RoleDTO) {
        RoleDTO createdRole = roleService.createRole(RoleDTO);
        return new ResponseEntity<>(createdRole, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<RoleDTO> updateRole( @Valid @RequestBody RoleDTO RoleDTO) {
        RoleDTO updatedRole = roleService.updateRole(RoleDTO);
        return ResponseEntity.ok(updatedRole);
    }

    @DeleteMapping("/delete/{RoleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long RoleId) {
        roleService.deleteRole(RoleId);
        return ResponseEntity.ok().build();
    }
}
