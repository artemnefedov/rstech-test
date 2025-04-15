package io.github.artemnefedov.rstech.repository;

import io.github.artemnefedov.rstech.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findByName(String name);

}
