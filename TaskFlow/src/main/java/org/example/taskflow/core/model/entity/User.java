package org.example.taskflow.core.model.entity;

import jakarta.persistence.*;

import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String surname;
    private String username;
    private String email;
    private String password;
    private Integer jetons;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToMany(mappedBy = "createdBy")
    @ToString.Exclude
    private List<Task> createdTasks;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<Task> tasks;

    @OneToMany(mappedBy = "user")
    @ToString.Exclude
    private List<JetonUsage> jetonUsages;
}
