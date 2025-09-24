package com.ingeduardo.demostore.dto;

import com.ingeduardo.demostore.model.enums.PostStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private LocalDateTime publishDate;
    private PostStatus status;
    private Set<String> tags;
    private String categoryName;
}
