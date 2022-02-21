package com.taskmanager.upper.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="tasks")
public class Tasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="description", columnDefinition = "TEXT")
    private String description;

    @Column(name="duedate")
    @DateTimeFormat (pattern="yyyy-MM-dd")
    private Date duedate;

    @Column(name="status")
    private String iscomplete;

    @ManyToOne(fetch = FetchType.EAGER)
    private Urgency urgency;

    @ManyToOne(fetch = FetchType.LAZY)
    private Important important;

    @Column (name="imoprtance")
    private Importance importance;

    public Importance getImportance(Importance importance) {
        return importance;
    }

    @Column(name="tasktype")
    @ManyToMany(fetch = FetchType.LAZY)
    private List<TaskType> taskType;
}
