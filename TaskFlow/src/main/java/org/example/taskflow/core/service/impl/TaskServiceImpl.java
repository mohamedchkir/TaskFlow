package org.example.taskflow.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.taskflow.common.helper.Validation.ValidateTask;
import org.example.taskflow.core.mapper.TaskMapper;
import org.example.taskflow.core.model.dto.StoreTaskDTO;
import org.example.taskflow.core.model.dto.TagDTO;
import org.example.taskflow.core.model.dto.TaskDTO;
import org.example.taskflow.core.model.dto.UserDTO;
import org.example.taskflow.core.model.entity.Task;
import org.example.taskflow.core.repository.TaskRepository;
import org.example.taskflow.core.service.TagService;
import org.example.taskflow.core.service.TaskService;
import org.example.taskflow.core.service.UserService;
import org.example.taskflow.shared.Enum.TaskStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskMapper taskMapper;
    private final TaskRepository taskRepository;
    private final ValidateTask validateTask;
    private final UserService userService;
    private final TagService tagService;

    @Override
    public List<TaskDTO> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        if (tasks.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No Task found");
        }
        return tasks.stream()
                .map(taskMapper::taskToTaskDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TaskDTO getTaskById(Long taskId) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found");
        }
        return taskMapper.taskToTaskDTO(task);
    }

    @Override
    public TaskDTO createTask(StoreTaskDTO storeTaskDTO) {
        // Set the status to TODO
        storeTaskDTO.setStatus(TaskStatus.TODO);
        // Validate the task
        validateTask.validateTask(storeTaskDTO);

        UserDTO createdBy = userService.getUserById(storeTaskDTO.getCreatedBy().getId());
        UserDTO assignedTo = userService.getUserById(storeTaskDTO.getUser().getId());

        if ("user".equals(createdBy.getRole().getName()) && !Objects.equals(createdBy.getRole().getName(), assignedTo.getRole().getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User cannot create a task");
        }

        storeTaskDTO.setCreatedBy(createdBy);
        storeTaskDTO.setUser(assignedTo);

        // Check if the tags are valid and set them in the DTO
        List<TagDTO> tagsDTO = storeTaskDTO.getTags();
        List<TagDTO> tags = new ArrayList<>();

        for (TagDTO tagDTO : tagsDTO) {
                TagDTO tag = tagService.getTagById(tagDTO.getId());
                tags.add(tag);
        }

        storeTaskDTO.setTags(tags);

        // Map and save the task
        Task task = taskMapper.INSTANCE.storeTaskDTOToTask(storeTaskDTO);
        taskRepository.save(task);

        // Map and return the task DTO
        return taskMapper.INSTANCE.taskToTaskDTO(task);
    }


    @Override
    public TaskDTO updateTask(Long taskId, TaskDTO taskDTO) {
        return null;
    }

    @Override
    public void deleteTask(Long taskId) {

    }
}
