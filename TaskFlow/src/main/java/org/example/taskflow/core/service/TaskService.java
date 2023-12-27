package org.example.taskflow.core.service;

import org.example.taskflow.core.model.dto.TaskDTO;

import java.util.List;

public interface TaskService {

    List<TaskDTO> getAllTasks();

    TaskDTO getTaskById(Long taskId);

    TaskDTO createTask(TaskDTO taskDTO);

    TaskDTO updateTask(Long taskId, TaskDTO taskDTO);

    void deleteTask(Long taskId);
}
