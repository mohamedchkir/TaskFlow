package org.example.taskflow.core.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.core.model.dto.UserDTO;

import java.io.Serializable;

@Getter
@Setter
public class UpdateTaskAssignToDto implements Serializable {
    @NotNull(message = "user assignTo cannot be null")
    private UserDTO user;
}
