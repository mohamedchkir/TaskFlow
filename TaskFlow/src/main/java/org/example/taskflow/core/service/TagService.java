package org.example.taskflow.core.service;

import org.example.taskflow.core.model.dto.TagDTO;

public interface TagService {

    TagDTO getTagById(Long tagId);
}
