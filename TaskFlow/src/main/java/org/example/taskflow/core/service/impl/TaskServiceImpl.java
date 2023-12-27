package org.example.taskflow.core.service.impl;

import org.example.taskflow.core.model.dto.TaskDTO;
import org.example.taskflow.core.service.TaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {
    @Override
    public List<TaskDTO> getAllTasks() {
        return null;
    }

    @Override
    public TaskDTO getTaskById(Long taskId) {
        return null;
    }

    @Override
    public TaskDTO createTask(TaskDTO taskDTO) {
        return null;
    }

    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        return null;
    }

    @Override
    public void deleteTask(Long taskId) {

    }
}
