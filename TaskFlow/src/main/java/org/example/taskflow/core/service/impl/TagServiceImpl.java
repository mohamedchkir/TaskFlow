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

import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public TagDTO getTagByName(String tagName) {

        Tag tag = tagRepository.findByName(tagName).orElse(null);
        if (tag == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        return TagMapper.INSTANCE.tagToTagDTO(tag);
    }

    @Override
    public List<TagDTO> getAllTags() {
        List<Tag> tags = tagRepository.findAll();
        if (tags.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tags not found");
        }
        return tags
                .stream()
                .map(TagMapper.INSTANCE::tagToTagDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TagDTO createTag(TagDTO tagDTO) {
        if (tagRepository.findByName(tagDTO.getName()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag already exists");
        }
        Tag tag = TagMapper.INSTANCE.tagDTOToTag(tagDTO);
        Tag savedTag = tagRepository.save(tag);
        return TagMapper.INSTANCE.tagToTagDTO(savedTag);
    }

    @Override
    public TagDTO updateTag(TagDTO tagDTO) {
        Tag tag = tagRepository.findById(tagDTO.getId()).orElse(null);
        if (tag == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        tag.setName(tagDTO.getName());
        if (tagRepository.findByName(tag.getName()).isPresent()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Tag already exists");
        }
        Tag savedTag = tagRepository.save(tag);
        return TagMapper.INSTANCE.tagToTagDTO(savedTag);

    }

    @Override
    public void deleteTag(Long tagId) {
        Tag tag = tagRepository.findById(tagId).orElse(null);
        if (tag == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tag not found");
        }
        tagRepository.delete(tag);
        throw new ResponseStatusException(HttpStatus.OK, "Tag deleted");

    }
}
