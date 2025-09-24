package com.ingeduardo.demostore.dto;

import com.ingeduardo.demostore.model.enums.PostStatus;
import lombok.Data;

import java.util.Set;

@Data
public class PostRequestDto {
    private String title;
    private String content;
    private Long authorId;
    private PostStatus status;
    private Set<String> tags;
    private Long categoryId;
}
