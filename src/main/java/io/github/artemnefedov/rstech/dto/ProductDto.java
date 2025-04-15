package io.github.artemnefedov.rstech.dto;

import io.github.artemnefedov.rstech.entity.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProductDto(
        long id,
        @NotBlank(message = "Наименование обязательно")
        String name,
        @NotBlank(message = "Описание обязательно")
        String description,
        @Min(value = 10, message = "Цена должна быть больше 10")
        double price,
        String imageUrl,
        @NotNull(message = "Категория обязательна")
        CategoryDto category,
        LocalDate createdAt,
        boolean isActive
) {

    public Product toEntity() {
        return new Product(
                id,
                name,
                description,
                BigDecimal.valueOf(price),
                imageUrl,
                category.toEntity(),
                createdAt,
                isActive
        );
    }
}
