package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.PostRequestDto;
import com.ingeduardo.demostore.dto.PostResponseDto;

import java.util.List;

public interface PostService {
    List<PostResponseDto> getAllPosts();
    PostResponseDto getPostById(Long id);
    PostResponseDto createPost(PostRequestDto requestDto);
    PostResponseDto updatePost(Long id, PostRequestDto requestDto);
    void deletePost(Long id);
}
