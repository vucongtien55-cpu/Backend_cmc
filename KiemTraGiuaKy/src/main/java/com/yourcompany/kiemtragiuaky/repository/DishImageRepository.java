package com.yourcompany.kiemtragiuaky.repository;
import com.yourcompany.kiemtragiuaky.entity.DishImage_BIT240224;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface DishImageRepository extends JpaRepository<DishImage_BIT240224, Long> {
    List<DishImage_BIT240224> findByDishId(Long dishId);
    boolean existsByDishIdAndIsThumbnailTrue(Long dishId);
}
