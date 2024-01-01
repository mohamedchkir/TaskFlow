package org.example.taskflow.core.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.shared.Enum.TaskPriority;
import org.example.taskflow.shared.Enum.TaskStatus;

import java.sql.Date;
import java.util.List;
@Setter
@Getter
public class StoreTaskDTO {
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must be at most 255 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description must be at most 1000 characters")
    private String description;

    @NotNull(message = "Priority cannot be null")
    private TaskPriority priority;

    @NotNull(message = "Status cannot be null")
    private TaskStatus status;

    @NotNull(message = "Assign date cannot be null")
    private Date assignDate;

    @NotNull(message = "Due date cannot be null")
    private Date dueDate;

    @Valid
    private UserDTO user;

    @Valid
    private UserDTO createdBy;

    @Valid
    @Size(min = 1, message = "At least one tag must be provided")
    private List<TagDTO> tags;
}
