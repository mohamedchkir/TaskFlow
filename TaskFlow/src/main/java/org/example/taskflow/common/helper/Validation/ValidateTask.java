package org.example.taskflow.common.helper.Validation;

import lombok.RequiredArgsConstructor;
import org.example.taskflow.core.mapper.TaskMapper;
import org.example.taskflow.core.model.dto.StoreTaskDTO;
import org.example.taskflow.core.model.dto.TagDTO;
import org.example.taskflow.core.model.entity.JetonUsage;
import org.example.taskflow.core.model.entity.Task;
import org.example.taskflow.core.model.entity.User;
import org.example.taskflow.core.repository.JetonUsageRepository;
import org.example.taskflow.core.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ValidateTask {

    private final JetonUsageRepository jetonUsageRepository;
    private final UserRepository userRepository;

    public void validateTask(StoreTaskDTO taskDTO) {
        validateTaskDuration(taskDTO.getAssignDate(), taskDTO.getDueDate());
        isTaskNotInThePast(taskDTO.getAssignDate());
        hasMinimumTags(taskDTO);
    }

    public void isTaskNotInThePast(Date assignDate) {
        Date today = Date.valueOf(LocalDate.now());
        if (assignDate.compareTo(today) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assign date must not be in the past");
        }
    }

    private void validateTaskDuration(Date assignDate, Date dueDate) {
        Duration duration = Duration.between(assignDate.toLocalDate().atStartOfDay(), dueDate.toLocalDate().atStartOfDay());
        if (assignDate.compareTo(dueDate) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Assign date must be before due date");
        }
        if (duration.toDays() > 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task duration must be 3 days maximum");
        }
    }

    private void hasMinimumTags(StoreTaskDTO taskDTO) {
        List<TagDTO> tags = taskDTO.getTags();

        if (tags.size() < 2) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Task must have at least 2 tags");
        }
    }

    public void performUsageJeton(Task task, User user, JetonUsage jetonUsage) {
        jetonUsage.setActionDate(LocalDateTime.now());
        jetonUsage.setUser(user);
        jetonUsage.setTask(task);

        jetonUsageRepository.save(jetonUsage);

        user.setJetons(user.getJetons() - 1);
        userRepository.save(user);

        TaskMapper.INSTANCE.taskToTaskDTO(task);
    }


}
