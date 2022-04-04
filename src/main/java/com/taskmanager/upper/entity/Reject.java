package com.taskmanager.upper.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "rejected")
@NoArgsConstructor
@AllArgsConstructor
public class Reject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String rejected;
    @ManyToOne(fetch = FetchType.EAGER)
    private AuthUsers author;
    @ManyToOne(fetch = FetchType.EAGER)
    private Tasks tasks;
    private String comment;
    private Date rejectDate;

}
