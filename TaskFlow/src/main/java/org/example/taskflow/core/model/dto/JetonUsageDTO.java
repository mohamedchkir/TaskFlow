package org.example.taskflow.core.model.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.example.taskflow.shared.Enum.JetonUsageAction;

import java.util.Date;

@Setter
@Getter
public class JetonUsageDTO {

    private Long id;
    private TaskDTO task;
    private UserDTO user;
    private JetonUsageAction action;
    private Date actionDate;
}
