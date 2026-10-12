package com.yourcompany.ontapgiuaky.repository;

import com.yourcompany.ontapgiuaky.entity.CourseCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseCategoryRepository extends JpaRepository<CourseCategory, Long> {
}

