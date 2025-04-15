package io.github.artemnefedov.rstech.service.impl;

import static io.github.artemnefedov.rstech.repository.specs.SearchOperation.EQUAL;
import static io.github.artemnefedov.rstech.repository.specs.SearchOperation.LIKE;

import io.github.artemnefedov.rstech.dto.ProductDto;
import io.github.artemnefedov.rstech.entity.Product;
import io.github.artemnefedov.rstech.repository.CategoryRepository;
import io.github.artemnefedov.rstech.repository.ProductRepository;
import io.github.artemnefedov.rstech.repository.specs.ProductSpec;
import io.github.artemnefedov.rstech.repository.specs.SearchCriteria;
import io.github.artemnefedov.rstech.repository.specs.SearchOperation;
import io.github.artemnefedov.rstech.service.ProductService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
            CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
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
    public Page<ProductDto> findProducts(String name, String categoryName, BigDecimal priceFrom,
            BigDecimal priceTo, Pageable pageable) {
        List<SearchCriteria> criteriaList = new ArrayList<>();

        if (name != null && !name.isEmpty()) {
            criteriaList.add(new SearchCriteria("name", name, LIKE));
        }
        if (categoryName != null && !categoryName.isEmpty()) {
            criteriaList.add(
                    new SearchCriteria("category", categoryRepository.findByName(categoryName),
                            EQUAL));
        }
        if (priceFrom != null) {
            criteriaList.add(new SearchCriteria("price", priceFrom, SearchOperation.GREATER_THAN));
        }
        if (priceTo != null) {
            criteriaList.add(new SearchCriteria("price", priceTo, SearchOperation.LESS_THAN));
        }
        ProductSpec spec = new ProductSpec(criteriaList);

        return productRepository.findAll(spec, pageable).map(Product::toDto);
    }

    @Override
    public ProductDto getById(long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Product with id " + id + " does not exist"))
                .toDto();
    }
}
