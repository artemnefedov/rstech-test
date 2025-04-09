package io.github.artemnefedov.rstech.service.impl;

import io.github.artemnefedov.rstech.dto.ProductDto;
import io.github.artemnefedov.rstech.entity.Product;
import io.github.artemnefedov.rstech.repository.ProductRepository;
import io.github.artemnefedov.rstech.repository.specs.ProductSpecification;
import io.github.artemnefedov.rstech.repository.specs.SearchCriteria;
import io.github.artemnefedov.rstech.repository.specs.SearchOperation;
import io.github.artemnefedov.rstech.service.ProductService;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public ProductDto create(ProductDto productDto) {
        return productRepository.save(productDto.toEntity()).toDto();
    }

    @Override
    public Boolean delete(long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        } else {
            throw new IllegalArgumentException("Product with id " + id + " does not exist");
        }
    }

    @Override
    public ProductDto update(ProductDto productDto) {
        if (productRepository.existsById(productDto.id())) {
            return productRepository.save(productDto.toEntity()).toDto();
        } else {
            throw new IllegalArgumentException("Product with id " + productDto.id() + " does not exist");
        }
    }

    @Override
    public List<ProductDto> search(String category, String name, String priceFrom, String priceTo) {
        ProductSpecification specification = new ProductSpecification();

        if (category != null && !category.isEmpty()) {
            specification.add(new SearchCriteria("category.name", category, SearchOperation.EQUAL));
        }

        if (name != null && !name.isEmpty()) {
            specification.add(new SearchCriteria("name", name, SearchOperation.EQUAL));
        }
        if (priceFrom != null && !priceFrom.isEmpty()) {
            specification.add(new SearchCriteria("price", priceFrom, SearchOperation.GREATER_THAN));
        }

        if (priceTo != null && !priceTo.isEmpty()) {
            specification.add(new SearchCriteria("price", priceTo, SearchOperation.LESS_THAN));
        }

        return productRepository.findAll(specification).stream()
                .map(Product::toDto)
                .toList();
    }
}
