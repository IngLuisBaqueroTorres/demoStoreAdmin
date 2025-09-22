package com.ingeduardo.demostore.service;

import com.ingeduardo.demostore.model.Brand;
import com.ingeduardo.demostore.repository.BrandRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService {

    private final BrandRepository repository;

    public BrandService(BrandRepository repository) {
        this.repository = repository;
    }

    public List<Brand> findAll() {
        return repository.findAll();
    }

    public Page<Brand> search(String name, String description, Pageable pageable) {
        return repository.search(name, description, pageable);
    }

    public Brand findById(String id) {
        return repository.findById(id).orElse(null);
    }

    public Brand save(Brand brand) {
        if (repository.existsByName(brand.getName())) {
            throw new RuntimeException("Brand with this name already exists.");
        }
        return repository.save(brand);
    }

    public Brand update(String id, Brand brandDetails) {
        Brand existingBrand = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Brand not found."));

        if (!existingBrand.getName().equals(brandDetails.getName()) && repository.existsByName(brandDetails.getName())) {
            throw new RuntimeException("Another brand with this name already exists.");
        }

        existingBrand.setName(brandDetails.getName());
        existingBrand.setDescription(brandDetails.getDescription());

        return repository.save(existingBrand);
    }

    public void delete(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Brand not found.");
        }
        repository.deleteById(id);
    }
}
