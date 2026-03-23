package com.bsuir.distcomp.mapper;

import com.bsuir.distcomp.dto.WriterRequestTo;
import com.bsuir.distcomp.dto.WriterResponseTo;
import com.bsuir.distcomp.entity.Writer;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface WriterMapper {

    Writer toEntity(WriterRequestTo dto);

    WriterResponseTo toDto(Writer entity);

}
