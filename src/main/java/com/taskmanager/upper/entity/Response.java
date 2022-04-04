package com.taskmanager.upper.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "response")
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String acceptance;
    @ManyToOne(fetch = FetchType.EAGER)
    private AuthUsers author;
    @ManyToOne(fetch = FetchType.EAGER)
    private Tasks tasks;
    private String comment;
    private Date plannedDate;

}
