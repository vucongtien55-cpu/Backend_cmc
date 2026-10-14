package com.example.quanlymonan.repository;

import com.example.quanlymonan.entity.Dish_MSSV_CUA_BAN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DishRepository extends JpaRepository<Dish_MSSV_CUA_BAN, Long>, JpaSpecificationExecutor<Dish_MSSV_CUA_BAN> {
    boolean existsByDishCategoryId(Long categoryId);
    boolean existsByNameIgnoreCaseAndDishCategoryId(String name, Long categoryId);
    boolean existsByNameIgnoreCaseAndDishCategoryIdAndIdNot(String name, Long categoryId, Long id);
}
