package org.example.taskflow.core.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TagDTO {

    private Long id;
    private String name;
}