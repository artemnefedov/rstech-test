package io.github.artemnefedov.rstech.service;

import io.github.artemnefedov.rstech.dto.CategoryDto;
import java.util.List;

public interface CategoryService {

    CategoryDto create(CategoryDto category);

    boolean delete(long id);

    List<CategoryDto> getAll();

    CategoryDto update(CategoryDto categoryDto);

    CategoryDto getById(long id);
}
