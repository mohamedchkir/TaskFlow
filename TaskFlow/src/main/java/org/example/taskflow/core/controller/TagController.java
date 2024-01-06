package org.example.taskflow.core.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.taskflow.core.model.dto.TagDTO;
import org.example.taskflow.core.service.TagService;
import org.example.taskflow.shared.Const.AppEndpoints;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(AppEndpoints.TAG_ENDPOINT)
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;

    @GetMapping
    public ResponseEntity<List<TagDTO>> getAllTags() {
        List<TagDTO> tags = tagService.getAllTags();
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/id/{tagId}")
    public ResponseEntity<TagDTO> getTagById( @PathVariable Long tagId) {
        TagDTO tag = tagService.getTagById(tagId);
        if (tag != null) {
            return new ResponseEntity<>(tag, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/name/{tagName}")
    public ResponseEntity<TagDTO> getTagByName(@PathVariable String tagName) {
        TagDTO tag = tagService.getTagByName(tagName);
        if (tag != null) {
            return new ResponseEntity<>(tag, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<TagDTO> createTag(@Valid @RequestBody TagDTO tagDTO) {
        TagDTO createdTag = tagService.createTag(tagDTO);
        return new ResponseEntity<>(createdTag, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<TagDTO> updateTag( @Valid @RequestBody TagDTO tagDTO) {
        TagDTO updatedTag = tagService.updateTag(tagDTO);
        return ResponseEntity.ok(updatedTag);
    }

    @DeleteMapping("/delete/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long tagId) {
        tagService.deleteTag(tagId);
        return ResponseEntity.ok().build();
    }
}
