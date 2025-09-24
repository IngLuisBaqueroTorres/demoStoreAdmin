package com.ingeduardo.demostore.service.impl;

import com.ingeduardo.demostore.dto.PostRequestDto;
import com.ingeduardo.demostore.dto.PostResponseDto;
import com.ingeduardo.demostore.exception.ResourceNotFoundException;
import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.model.Post;
import com.ingeduardo.demostore.model.User;
import com.ingeduardo.demostore.repository.CategoryRepository;
import com.ingeduardo.demostore.repository.PostRepository;
import com.ingeduardo.demostore.repository.UserRepository;
import com.ingeduardo.demostore.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public List<PostResponseDto> getAllPosts() {
        return postRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));
        return convertToDto(post);
    }

    @Override
    public PostResponseDto createPost(PostRequestDto requestDto) {
        Post post = new Post();
        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setStatus(requestDto.getStatus());
        post.setTags(requestDto.getTags());

        if (requestDto.getAuthorId() != null) {
            User author = userRepository.findById(requestDto.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + requestDto.getAuthorId()));
            post.setAuthor(author);
        }

        if (requestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDto.getCategoryId()));
            post.setCategory(category);
        }

        Post savedPost = postRepository.save(post);
        return convertToDto(savedPost);
    }

    @Override
    public PostResponseDto updatePost(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found with id: " + id));

        post.setTitle(requestDto.getTitle());
        post.setContent(requestDto.getContent());
        post.setStatus(requestDto.getStatus());
        post.setTags(requestDto.getTags());

        if (requestDto.getAuthorId() != null) {
            User author = userRepository.findById(requestDto.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + requestDto.getAuthorId()));
            post.setAuthor(author);
        }

        if (requestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(requestDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + requestDto.getCategoryId()));
            post.setCategory(category);
        }

        Post updatedPost = postRepository.save(post);
        return convertToDto(updatedPost);
    }

    @Override
    public void deletePost(Long id) {
        if (!postRepository.existsById(id)) {
            throw new ResourceNotFoundException("Post not found with id: " + id);
        }
        postRepository.deleteById(id);
    }

    private PostResponseDto convertToDto(Post post) {
        PostResponseDto dto = new PostResponseDto();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setPublishDate(post.getPublishDate());
        dto.setStatus(post.getStatus());
        dto.setTags(post.getTags());

        if (post.getAuthor() != null) {
            dto.setAuthorName(post.getAuthor().getName());
        }

        if (post.getCategory() != null) {
            dto.setCategoryName(post.getCategory().getName());
        }
        return dto;
    }
}
