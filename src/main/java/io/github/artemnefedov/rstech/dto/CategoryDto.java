package io.github.artemnefedov.rstech.dto;

import io.github.artemnefedov.rstech.entity.Category;
import jakarta.validation.constraints.NotBlank;

public record CategoryDto(
        long id,
        @NotBlank(message = "Наименование обязательно")
        String name,
        @NotBlank(message = "Описание обязательно")
        String description
) {

    public Category toEntity() {
        return new Category(id, name, description);
    }
}
