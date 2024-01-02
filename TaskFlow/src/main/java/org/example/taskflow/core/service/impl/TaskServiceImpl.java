package org.example.taskflow.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.taskflow.common.helper.Validation.ValidateTask;
import org.example.taskflow.core.mapper.TaskMapper;
import org.example.taskflow.core.mapper.UserMapper;
import org.example.taskflow.core.model.dto.*;
import org.example.taskflow.core.model.entity.JetonUsage;
import org.example.taskflow.core.model.entity.Task;
import org.example.taskflow.core.model.entity.User;
import org.example.taskflow.core.repository.JetonUsageRepository;
import org.example.taskflow.core.repository.TaskRepository;
import org.example.taskflow.core.repository.UserRepository;
import org.example.taskflow.core.service.TagService;
import org.example.taskflow.core.service.TaskService;
import org.example.taskflow.core.service.UserService;
import org.example.taskflow.shared.Enum.JetonUsageAction;
import org.example.taskflow.shared.Enum.TaskStatus;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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
    public void deleteTask(Long taskId, Long userId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"task not found"));

        UserDTO user = userService.getUserById(userId);
        UserDTO taskCreatedBy = userService.getUserById(task.getCreatedBy().getId());

        if (!Objects.equals(taskCreatedBy.getId(), user.getId()) && !"admin".equals(user.getRole().getName())) {
            if (!Objects.equals(task.getUser().getId(), user.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "you cannot delete this task");
            }

            if (task.getJetonUsage()!= null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"this task cannot be deleted, replaced task");
            }

            if (user.getJetons() == 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"you dont have any jeton to make this action");
            }

            JetonUsageDTO lastDeletedJetonUsage = user.getJetonUsages().stream()
                    .filter(jetonUsage -> jetonUsage.getAction().equals(JetonUsageAction.DELETE))
                    .max(Comparator.comparing(JetonUsageDTO::getActionDate)).orElse(null);

            if (lastDeletedJetonUsage != null) {
                Date actionDate = lastDeletedJetonUsage.getActionDate();

                // Convert the Date to Instant and then to LocalDate
                LocalDate lastActionDate = actionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                if (lastActionDate.plusDays(30).isAfter(LocalDate.now())) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"You cannot use jeton to delete; the last action of delete is within 30 days");
                }
            }

            User user1 = UserMapper.INSTANCE.userDTOToUser(user);

            JetonUsage jetonUsage = new JetonUsage();
            jetonUsage.setAction(JetonUsageAction.DELETE);

            validateTask.performUsageJeton(task, user1, jetonUsage);

            throw new ResponseStatusException(HttpStatus.OK,"wait for admin to approve");

        }

        taskRepository.delete(task);
        throw new ResponseStatusException(HttpStatus.OK,"task deleted");

    }
}
