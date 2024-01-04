package org.example.taskflow.core.service;

import org.example.taskflow.core.model.dto.*;

import java.util.List;

public interface TaskService {

    List<TaskDTO> getAllTasks();

    TaskDTO getTaskById(Long taskId);

    TaskDTO createTask(StoreTaskDTO storeTaskDTO);

    TaskDTO updateStatus(Long taskId, updateTaskStatusDto updateTaskStatusDto , UserDTO userDTO);

    TaskDTO updateAssignTo(Long id, UpdateTaskAssignToDto updateTaskAssignToDto, UserDTO User);

    void deleteTask(Long taskId, Long userId);

    TaskDTO updateTask(Long id, UpdateTaskDto updateTaskDto, UserDTO User);
}
