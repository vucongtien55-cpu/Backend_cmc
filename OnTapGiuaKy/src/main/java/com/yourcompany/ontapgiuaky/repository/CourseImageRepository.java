package com.yourcompany.ontapgiuaky.repository;

import com.yourcompany.ontapgiuaky.entity.CourseImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseImageRepository extends JpaRepository<CourseImage, Long> {
}

