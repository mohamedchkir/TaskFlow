package org.example.taskflow.core.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.shared.Enum.TaskStatus;

@Getter
@Setter
public class updateTaskStatusDto{
    @NotNull(message = "status cannot be null")
    private TaskStatus status;
}
