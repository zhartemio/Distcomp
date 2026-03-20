package com.bsuir.distcomp.mapper;

import com.bsuir.distcomp.dto.MarkerRequestTo;
import com.bsuir.distcomp.dto.MarkerResponseTo;
import com.bsuir.distcomp.entity.Marker;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface MarkerMapper {

    @Mapping(target = "id", ignore = true)
    Marker toEntity(MarkerRequestTo dto);

    MarkerResponseTo toDto(Marker entity);

}
