package io.github.artemnefedov.rstech.controller;

import io.github.artemnefedov.rstech.dto.IndexResponse;
import io.github.artemnefedov.rstech.dto.ProductDto;
import io.github.artemnefedov.rstech.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Product rest controller", description = "REST-контрроллер для работы с товарами")
@RestController
@RequestMapping("/api/v1/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            summary = "Получение всех товаров",
            description = "Получение всех товаров с возможностью фильтрации по имени, категории и цене"
    )
    @GetMapping
    public ResponseEntity<IndexResponse> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String categoryName,
            @RequestParam(required = false) BigDecimal priceFrom,
            @RequestParam(required = false) BigDecimal priceTo
    ) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<ProductDto> productsPage = productService.findProducts(name, categoryName, priceFrom,
                priceTo, pageable);
        return ResponseEntity.ok(
                new IndexResponse(productsPage.getContent(), productsPage.getTotalPages()));
    }

    @Operation(summary = "Получение товара по id", description = "Получение товара по id")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    @Operation(summary = "Создание товара", description = "Создание товара")
    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.create(productDto));
    }

    @Operation(summary = "Удаление товара", description = "Удаление товара по id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteProduct(@PathVariable long id) {
        return ResponseEntity.ok(productService.delete(id));
    }

    @Operation(summary = "Обновление товара", description = "Обновление товара")
    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.ok(productService.update(productDto));
    }
}
