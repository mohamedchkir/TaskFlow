package org.example.taskflow.core.mapper;

import org.example.taskflow.core.model.dto.TagDTO;
import org.example.taskflow.core.model.entity.Tag;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TagMapper {

    TagMapper INSTANCE = Mappers.getMapper(TagMapper.class);

    TagDTO tagToTagDTO(Tag tag);

    Tag tagDTOToTag(TagDTO tagDTO);
}
