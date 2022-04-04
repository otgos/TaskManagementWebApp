package com.taskmanager.upper.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Serializable {

    private Long id;
    private String commentText;
    private Long annId;
    private String fullName;
    private Long userId;
    private Date postDate;
}
