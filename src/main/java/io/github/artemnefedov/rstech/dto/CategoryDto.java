package io.github.artemnefedov.rstech.dto;

import io.github.artemnefedov.rstech.entity.Category;

public record CategoryDto(long id, String name, String description) {

    public Category toEntity() {
        return new Category(id, name, description);
    }
}
