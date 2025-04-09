package io.github.artemnefedov.rstech.repository.specs;

import io.github.artemnefedov.rstech.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.jpa.domain.Specification;

public class ProductSpecification implements Specification<Product> {

    private final List<SearchCriteria> params;

    public ProductSpecification() {
        this.params = new ArrayList<>();
    }

    public void add(SearchCriteria criteria) {
        params.add(criteria);
    }


    @Override
    public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query,
            CriteriaBuilder builder) {

        List<Predicate> predicates = new ArrayList<>();

        params.forEach(criteria -> {
            if (criteria.operation().equals(SearchOperation.GREATER_THAN)) {
                predicates.add(builder.greaterThan(
                        root.get(criteria.key()), criteria.value().toString()
                ));
            } else if (criteria.operation().equals(SearchOperation.LESS_THAN)) {
                predicates.add(builder.lessThan(
                        root.get(criteria.key()), criteria.value().toString()
                ));
            } else if (criteria.operation().equals(SearchOperation.EQUAL)) {
                predicates.add(builder.equal(
                        root.get(criteria.key()), criteria.value().toString()
                ));
            }
        });

        return builder.and(predicates.toArray(new Predicate[0]));
    }
}

