package io.github.artemnefedov.rstech.service;

import io.github.artemnefedov.rstech.dto.ProductDto;
import java.util.List;

public interface ProductService {

    ProductDto create(ProductDto productDto);

    Boolean delete(long id);

    ProductDto update(ProductDto productDto);

    List<ProductDto> search(String category, String name, String priceFrom, String priceTo);
}
