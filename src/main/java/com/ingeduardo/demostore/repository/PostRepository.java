package com.ingeduardo.demostore.repository;

import com.ingeduardo.demostore.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
