package com.taskmanager.upper.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "mail")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String subject;
    @Column(columnDefinition = "TEXT")
    private String mail;
    @ManyToOne
    private AuthUsers sender;
    @ManyToOne
    private AuthUsers receiver;
    private String email;
    private Date sendDate;

}
