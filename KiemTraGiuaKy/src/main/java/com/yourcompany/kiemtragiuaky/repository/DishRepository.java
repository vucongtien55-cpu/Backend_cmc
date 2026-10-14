package com.yourcompany.kiemtragiuaky.repository;

import com.yourcompany.kiemtragiuaky.entity.Dish_BIT240224;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DishRepository extends JpaRepository<Dish_BIT240224, Long>, JpaSpecificationExecutor<Dish_BIT240224> {
    boolean existsByDishCategoryId(Long categoryId);
    boolean existsByNameIgnoreCaseAndDishCategoryId(String name, Long categoryId);
    boolean existsByNameIgnoreCaseAndDishCategoryIdAndIdNot(String name, Long categoryId, Long id);
}
