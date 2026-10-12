package com.yourcompany.ontapgiuaky.repository;
import com.yourcompany.ontapgiuaky.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("SELECT COUNT(c) > 0 FROM Course c WHERE c.category.id = :categoryId")
    boolean existsByCategoryId(@Param("categoryId") Long categoryId);
    @Query("SELECT COUNT(c) > 0 FROM Course c WHERE c.category.id = :categoryId AND LOWER(c.name) = LOWER(:name) AND (:excludeCourseId IS NULL OR c.id <> :excludeCourseId)")
    boolean existsByCategoryIdAndNameIgnoreCaseAndIdNot(
            @Param("categoryId") Long categoryId,
            @Param("name") String name,
            @Param("excludeCourseId") Long excludeCourseId
    );
    @Query("SELECT c FROM Course c WHERE (:keyword IS NULL OR :keyword = '' OR LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))) AND (:categoryId IS NULL OR c.category.id = :categoryId) AND (:isActive IS NULL OR c.isActive = :isActive) AND (:minFee IS NULL OR c.tuitionFee >= :minFee) AND (:maxFee IS NULL OR c.tuitionFee <= :maxFee)")
    Page<Course> searchCourses(
            @Param("keyword") String keyword,
            @Param("categoryId") Long categoryId,
            @Param("isActive") Boolean isActive,
            @Param("minFee") Double minFee,
            @Param("maxFee") Double maxFee,
            Pageable pageable
    );
}
