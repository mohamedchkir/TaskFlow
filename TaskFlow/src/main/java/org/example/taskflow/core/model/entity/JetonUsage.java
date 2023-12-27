package org.example.taskflow.core.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.taskflow.shared.Enum.JetonUsageAction;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JetonUsage {

    @Id
    @Column(name = "task_id")
    private Long taskId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private JetonUsageAction action;

    @Column(name = "action_date")
    private Date actionDate;

    @OneToOne
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

}