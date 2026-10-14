package com.example.quanlymonan.repository;
import com.example.quanlymonan.entity.DishImage_MSSV_CUA_BAN;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DishImageRepository extends JpaRepository<DishImage_MSSV_CUA_BAN, Long> {
    List<DishImage_MSSV_CUA_BAN> findByDishId(Long dishId);
    boolean existsByDishIdAndIsThumbnailTrue(Long dishId);
}
