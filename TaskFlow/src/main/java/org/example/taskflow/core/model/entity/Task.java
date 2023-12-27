package org.example.taskflow.core.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.example.taskflow.shared.Enum.TaskPriority;
import org.example.taskflow.shared.Enum.TaskStatus;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @Enumerated(EnumType.STRING)
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Column(name = "assign_date")
    private Date assignDate;

    @Column(name = "due_date")
    private Date dueDate;

    @ManyToOne
    @JoinColumn(name = "created_by_id")  // Different name for createdBy join column
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "user_id")  // Different name for user join column
    private User user;

    @OneToOne(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private JetonUsage jetonUsage;

    @ManyToMany
    @JoinTable(
            name = "task_tag",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private List<Tag> tags;
}
