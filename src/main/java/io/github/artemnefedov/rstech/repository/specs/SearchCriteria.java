package io.github.artemnefedov.rstech.repository.specs;

public record SearchCriteria(
        String key,
        Object value,
        SearchOperation operation
) {

}
