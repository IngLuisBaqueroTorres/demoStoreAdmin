package com.ingeduardo.demostore.config;

import com.ingeduardo.demostore.model.Brand;
import com.ingeduardo.demostore.repository.BrandRepository;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DefaultBrandInitializer {

    private static final Logger logger = LoggerFactory.getLogger(DefaultBrandInitializer.class);
    private final BrandRepository brandRepository;
    private static final String DEFAULT_BRAND_NAME = "Otros";

    public DefaultBrandInitializer(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    @PostConstruct
    public void ensureDefaultBrand() {
        if (!brandRepository.existsByName(DEFAULT_BRAND_NAME)) {
            Brand b = new Brand();
            b.setName(DEFAULT_BRAND_NAME);
            b.setDescription("Default brand for uncategorized products");
            brandRepository.save(b);
            logger.info("Created default brand '{}'", DEFAULT_BRAND_NAME);
        } else {
            logger.info("Default brand '{}' already exists", DEFAULT_BRAND_NAME);
        }
    }
}