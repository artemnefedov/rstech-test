package io.github.artemnefedov.rstech.service.impl;

import io.github.artemnefedov.rstech.dto.CategoryDto;
import io.github.artemnefedov.rstech.entity.Category;
import io.github.artemnefedov.rstech.repository.CategoryRepository;
import io.github.artemnefedov.rstech.service.CategoryService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public CategoryDto create(CategoryDto category) {
        return categoryRepository.save(category.toEntity()).toDto();
    }

    @Override
    public boolean delete(long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        } else {
            throw new IllegalArgumentException("Category with id " + id + " does not exist");
        }
    }

    @Override
    public List<CategoryDto> getAll() {
        return categoryRepository.findAll().stream()
                .map(Category::toDto)
                .toList();
    }

    @Override
    public CategoryDto update(CategoryDto categoryDto) {
        if (categoryRepository.existsById(categoryDto.id())) {
            return categoryRepository.save(categoryDto.toEntity()).toDto();
        } else {
            throw new IllegalArgumentException("Category with id " + categoryDto.id() + " does not exist");
        }
    }
}
