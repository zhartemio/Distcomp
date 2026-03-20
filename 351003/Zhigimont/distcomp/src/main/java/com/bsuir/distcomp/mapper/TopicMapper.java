package com.bsuir.distcomp.mapper;

import com.bsuir.distcomp.dto.TopicRequestTo;
import com.bsuir.distcomp.dto.TopicResponseTo;
import com.bsuir.distcomp.entity.Topic;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface TopicMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "modified", ignore = true)
    @Mapping(target = "writer", ignore = true)
    Topic toEntity(TopicRequestTo dto);

    @Mapping(source = "writer.id", target = "writerId")
    TopicResponseTo toDto(Topic entity);
}

