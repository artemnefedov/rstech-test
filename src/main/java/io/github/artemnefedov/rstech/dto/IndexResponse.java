package io.github.artemnefedov.rstech.dto;

import java.util.List;

public record IndexResponse(
        List<ProductDto> products,
        int totalPages
) {

}
