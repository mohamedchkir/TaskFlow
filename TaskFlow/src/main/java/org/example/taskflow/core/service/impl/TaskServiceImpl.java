package org.example.taskflow.core.service.impl;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.taskflow.core.mapper.TaskMapper;
import org.example.taskflow.core.model.dto.TaskDTO;
import org.example.taskflow.core.model.entity.Task;
import org.example.taskflow.core.repository.TaskRepository;
import org.example.taskflow.core.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Task found");
        }
        return tasks.stream()
                .map(taskMapper::taskToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        return taskMapper.taskToTaskDTO(task);
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
