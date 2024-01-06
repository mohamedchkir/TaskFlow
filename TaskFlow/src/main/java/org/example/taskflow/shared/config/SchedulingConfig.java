package org.example.taskflow.shared.config;

import lombok.RequiredArgsConstructor;
import org.example.taskflow.core.service.TaskService;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class SchedulingConfig {
    private final TaskService taskService;

    @Scheduled(cron = "0/15 * * * * *")
    public void updateOverdueTasks() {
        taskService.ChangeStatusToOutdated();
        System.out.println("here");
    }
}
