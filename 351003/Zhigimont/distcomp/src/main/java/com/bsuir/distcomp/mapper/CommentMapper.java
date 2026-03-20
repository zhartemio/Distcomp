package com.bsuir.distcomp.mapper;

import com.bsuir.distcomp.dto.CommentRequestTo;
import com.bsuir.distcomp.dto.CommentResponseTo;
import com.bsuir.distcomp.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;


@Mapper(componentModel = "spring")
@Component
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "topic", ignore = true)
    Comment toEntity(CommentRequestTo dto);

    @Mapping(source = "topic.id", target = "topicId")
    CommentResponseTo toDto(Comment entity);
}

