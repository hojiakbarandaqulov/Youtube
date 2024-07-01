package org.example.repository;


import org.example.entity.category.CategoryEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface CategoryRepository extends CrudRepository<CategoryEntity , Integer> {

    Optional<CategoryEntity> findByName(String name);
}
