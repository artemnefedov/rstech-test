package io.github.artemnefedov.rstech.service;

import io.github.artemnefedov.rstech.dto.ProductDto;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    ProductDto create(ProductDto productDto);

    Boolean delete(long id);

    ProductDto update(ProductDto productDto);

    Page<ProductDto> findProducts(
            String name,
            String categoryName,
            BigDecimal priceFrom,
            BigDecimal priceTo,
            Pageable pageable
    );

    ProductDto getById(long id);
}
