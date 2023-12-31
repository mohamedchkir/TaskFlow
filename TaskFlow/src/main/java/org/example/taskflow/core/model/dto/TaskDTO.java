package org.example.taskflow.core.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.shared.Enum.TaskPriority;
import org.example.taskflow.shared.Enum.TaskStatus;

import java.sql.Date;
import java.util.List;

@Setter
@Getter
public class TaskDTO {

    private Long id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private Date assignDate;
    private Date dueDate;
    private UserDTO createdBy;
    private UserDTO user;
    private List<TagDTO> tags;

}