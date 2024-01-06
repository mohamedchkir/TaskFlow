package org.example.taskflow.core.service;

import org.example.taskflow.core.model.dto.TagDTO;

import java.util.List;

public interface TagService {

    TagDTO getTagById(Long tagId);

    TagDTO getTagByName(String tagName);

    List<TagDTO> getAllTags();

    TagDTO createTag(TagDTO tagDTO);

    TagDTO updateTag(TagDTO tagDTO);

    void deleteTag(Long tagId);
}
