package org.example.taskflow.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.taskflow.common.helper.Validation.ValidateTask;
import org.example.taskflow.core.mapper.TaskMapper;
import org.example.taskflow.core.mapper.UserMapper;
import org.example.taskflow.core.model.dto.*;
import org.example.taskflow.core.model.entity.JetonUsage;
import org.example.taskflow.core.model.entity.Tag;
import org.example.taskflow.core.model.entity.Task;
import org.example.taskflow.core.model.entity.User;
import org.example.taskflow.core.repository.JetonUsageRepository;
import org.example.taskflow.core.repository.TagRepository;
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
    private final TagRepository tagRepository;


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
    public TaskDTO updateStatus(Long taskId, updateTaskStatusDto updateTaskStatusDto, UserDTO userDTO) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"task not found"));

        UserDTO user = userService.getUserById(userDTO.getId());

        if (!Objects.equals(task.getUser().getId(),user.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE,"you cannot update status of this task, you dont have the right permission");
        }

        if (updateTaskStatusDto.getStatus().equals(TaskStatus.OUTDATED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"you cannot set status to outdated");
        }

        if (task.getStatus() != updateTaskStatusDto.getStatus() && updateTaskStatusDto.getStatus().equals(TaskStatus.DONE) && task.getDueDate().before(new Date())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"you cannot set status to done after due date");
        }

        task.setStatus(updateTaskStatusDto.getStatus());

        Task update = taskRepository.save(task);
        return TaskMapper.INSTANCE.taskToTaskDTO(update);
    }

    @Override
    public TaskDTO updateAssignTo(Long id, UpdateTaskAssignToDto updateTaskAssignToDto, UserDTO User) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task not found"));
        UserDTO assignTo = userService.getUserById(updateTaskAssignToDto.getUser().getId());
        UserDTO authUser = userService.getUserById(User.getId());
        if (!Objects.equals(authUser.getRole().getName(), "admin")) {
            if (!Objects.equals(task.getUser().getId(), authUser.getId())) {
                throw new RuntimeException("you cannot update a task not assign to you");
            }

            if (task.getJetonUsage() != null) {
                throw new RuntimeException("this task cannot be updated, replaced task");
            }

            if (authUser.getJetons() == 0) {
                throw new RuntimeException("you dont have any jeton to make this action");
            }

            JetonUsage jetonUsage = new JetonUsage();
            jetonUsage.setAction(JetonUsageAction.UPDATE);

            User user = UserMapper.INSTANCE.userDTOToUser(authUser);

            validateTask.performUsageJeton(task, user, jetonUsage);

            return TaskMapper.INSTANCE.taskToTaskDTO(task);
        }

        User userAssignedTo = UserMapper.INSTANCE.userDTOToUser(assignTo);
        task.setUser(userAssignedTo);
        taskRepository.save(task);
        return TaskMapper.INSTANCE.taskToTaskDTO(task);
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

    @Override
    public TaskDTO updateTask(Long id, UpdateTaskDto updateTaskDto, UserDTO User) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task not found"));
        UserDTO authUser = userService.getUserById(User.getId());

        if (authUser.getRole().getName().equals("user") && !Objects.equals(task.getCreatedBy().getId(), authUser.getId())) {
            throw new RuntimeException("you cannot update this task, you dont have the right permission");
        }

        if (isDurationMoreThanThreeDays(updateTaskDto.getAssignDate(), updateTaskDto.getDueDate())) {
            throw new RuntimeException("invalid duration or more than 3 days");
        }

        Task newTask = TaskMapper.INSTANCE.updateTaskDTOToTask(updateTaskDto);
        newTask.setId(id);
        newTask.setStatus(task.getStatus());
        newTask.setUser(task.getUser());
        newTask.setCreatedBy(task.getCreatedBy());
        newTask.setJetonUsage(task.getJetonUsage());

        // retrieve and validate tags:
        List<Tag> tags = updateTaskDto.getTags()
                .stream()
                .map(tagDto -> tagRepository.findById(tagDto.getId()).orElseThrow(() -> new RuntimeException("tag not found: " + tagDto.getId())))
                .toList();
        task.setTags(tags);
        newTask.setTags(tags);

        Task update = taskRepository.save(newTask);
        return TaskMapper.INSTANCE.taskToTaskDTO(update);
    }

    private Boolean isDurationMoreThanThreeDays(LocalDate startDate, LocalDate endDate) {
        return startDate.isAfter(endDate) || startDate.plusDays(3).isBefore(endDate);
    }

    public void ChangeStatusToOutdated() {
        List<Task> tasks = taskRepository.findAll();
        for (Task task : tasks) {
            if (task.getDueDate().before(new Date()) && task.getStatus() != TaskStatus.DONE) {
                task.setStatus(TaskStatus.OUTDATED);
                taskRepository.save(task);
            }
        }
    }

}
