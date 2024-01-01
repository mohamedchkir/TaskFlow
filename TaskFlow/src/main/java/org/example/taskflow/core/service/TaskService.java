package org.example.taskflow.core.service;

import org.example.taskflow.core.model.dto.StoreTaskDTO;
import org.example.taskflow.core.model.dto.TaskDTO;

import java.util.List;

public interface TaskService {

    List<TaskDTO> getAllTasks();

    TaskDTO getTaskById(Long taskId);

    TaskDTO createTask(StoreTaskDTO storeTaskDTO);

    TaskDTO updateTask(Long taskId, TaskDTO taskDTO);

    void deleteTask(Long taskId);
}
