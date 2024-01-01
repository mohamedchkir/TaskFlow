package org.example.taskflow.core.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.taskflow.core.mapper.TagMapper;
import org.example.taskflow.core.model.dto.TagDTO;
import org.example.taskflow.core.model.entity.Tag;
import org.example.taskflow.core.repository.TagRepository;
import org.example.taskflow.core.service.TagService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;
    @Override
    public TagDTO getTagById(Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (tag == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        return TagMapper.INSTANCE.tagToTagDTO(tag);
    }
}
