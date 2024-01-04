package org.example.taskflow.core.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.taskflow.core.model.dto.*;
import org.example.taskflow.core.service.TaskService;
import org.example.taskflow.shared.Const.AppEndpoints;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(AppEndpoints.TASK_ENDPOINT)
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @GetMapping
    public ResponseEntity<List<TaskDTO>> getAllTasks() {
        List<TaskDTO> tasks = taskService.getAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTaskById(@PathVariable Long taskId) {
        TaskDTO task = taskService.getTaskById(taskId);
        if (task != null) {
            return new ResponseEntity<>(task, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@Valid @RequestBody StoreTaskDTO taskDTO) {
        TaskDTO createdTask = taskService.createTask(taskDTO);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(
            @PathVariable Long taskId,
            @RequestBody UpdateTaskDto updateTaskDTO,
            @RequestBody UserDTO userDTO) {

        try {
            TaskDTO updatedTask = taskService.updateTask(taskId, updateTaskDTO, userDTO);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @DeleteMapping("/{taskId}/{userId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId , @PathVariable Long userId) {
        taskService.deleteTask(taskId , userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskDTO> updateTaskStatus(
            @PathVariable Long taskId,
            @RequestBody updateTaskStatusDto updateTaskStatusDTO,
            @RequestBody UserDTO userDTO) {

        try {
            TaskDTO updatedTask = taskService.updateStatus(taskId, updateTaskStatusDTO, userDTO);
            return ResponseEntity.ok(updatedTask);
        } catch (ResponseStatusException ex) {
            return ResponseEntity.status(ex.getStatusCode()).body(null);
        }
    }

    @PatchMapping("/{taskId}/assign-to")
    public ResponseEntity<TaskDTO> updateAssignTo(
            @PathVariable Long taskId,
            @RequestBody UpdateTaskAssignToDto updateTaskAssignToDTO,
            @RequestBody UserDTO userDTO) {

        try {
            TaskDTO updatedTask = taskService.updateAssignTo(taskId, updateTaskAssignToDTO, userDTO);
            return ResponseEntity.ok(updatedTask);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
