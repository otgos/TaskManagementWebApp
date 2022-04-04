package com.taskmanager.upper.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "submit")
@NoArgsConstructor
@AllArgsConstructor
public class Submit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String fileName;
    @ManyToOne(fetch = FetchType.EAGER)
    private AuthUsers author;
    @ManyToOne(fetch = FetchType.EAGER)
    private Tasks tasks;
    private String comment;
    private Date submittedDate;
    private byte[] content;
    private Long size;



}
