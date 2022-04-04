package com.taskmanager.upper.mapper;

import com.taskmanager.upper.dto.CommentDto;
import com.taskmanager.upper.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel= "spring")
public interface CommentMapper {

    @Mapping(target  = "commentText", source = "comment")
    @Mapping(target = "annId", source = "announcement.id")
    @Mapping(target = "fullName", source = "author.fullName")
    @Mapping(target = "userId", source = "author.id")
    CommentDto toCommentDto(Comment comment);

    List<CommentDto> toCommentDtoList (List<Comment> commentList);
}
