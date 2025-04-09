package io.github.artemnefedov.rstech.dto;

import io.github.artemnefedov.rstech.entity.Product;
import java.time.LocalDate;

public record ProductDto(
        long id,
        String name,
        String description,
        String price,
        String imageUrl,
        CategoryDto category,
        LocalDate createdAt,
        boolean isActive
) {

        public Product toEntity() {
                return new Product(
                        id,
                        name,
                        description,
                        price,
                        imageUrl,
                        createdAt,
                        isActive
                );
        }
}
