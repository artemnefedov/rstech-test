package io.github.artemnefedov.rstech.repository.specs;

import io.github.artemnefedov.rstech.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpec implements Specification<Product> {

    private final List<SearchCriteria> params;

    public ProductSpec(List<SearchCriteria> params) {
        this.params = params;
    }

    public void add(SearchCriteria criteria) {
        params.add(criteria);
    }

    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query,
            CriteriaBuilder builder) {
        return params
                .stream()
                .map(criteria ->
                        switch (criteria.operation()) {
                            case LIKE -> builder.like(
                                    builder.lower(root.get(criteria.key()).as(String.class)),
                                    "%" + criteria.value().toString().toLowerCase() + "%"
                            );
                            case EQUAL -> builder.equal(root.get(criteria.key()), criteria.value());
                            case GREATER_THAN -> builder.greaterThan(root.get(criteria.key()),
                                    criteria.value().toString());
                            case LESS_THAN -> builder.lessThan(root.get(criteria.key()),
                                    criteria.value().toString());
                        }
                )
                .reduce(builder::and)
                .orElseGet(builder::conjunction);
    }
}
