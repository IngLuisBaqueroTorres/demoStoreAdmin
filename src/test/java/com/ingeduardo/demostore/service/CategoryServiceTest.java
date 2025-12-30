package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.dto.CategoryRequestDto;
import com.ingeduardo.demostore.model.Category;
import com.ingeduardo.demostore.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void cleanup() {
        categoryRepository.deleteAll();
    }

    @Test
    void save_WithBlankParentId_SavesAsTopLevelCategory() {
        CategoryRequestDto dto = new CategoryRequestDto();
        dto.setName("Electrónica test1");
        dto.setDescription("Dispositivos y gadgets electrónicos");
        dto.setParentId(""); // simula lo que manda el front

        Category saved = categoryService.save(dto);

        assertNotNull(saved.getId(), "Should have generated id");
        assertNull(saved.getParent(), "Parent must be null for blank parentId");
        assertEquals("Electrónica test1", saved.getName());
    }
}