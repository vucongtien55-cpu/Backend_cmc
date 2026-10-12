package com.yourcompany.ontapgiuaky.service;
import com.yourcompany.ontapgiuaky.entity.Course;
import com.yourcompany.ontapgiuaky.entity.CourseImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface CourseService {
    Course findById(Long id);
    Course saveCourse(Course course);
    Course updateCourse(Long id, Course course);
    void deleteCourseCategory(Long categoryId);
    CourseImage addCourseImage(Long courseId, String imageUrl);
    void setThumbnail(Long courseId, Long imageId);
    Page<Course> searchCourses(String keyword, Long categoryId, Boolean isActive,
                               Double minFee, Double maxFee, Pageable pageable);
}
