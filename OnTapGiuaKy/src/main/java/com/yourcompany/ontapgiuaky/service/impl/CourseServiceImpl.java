package com.yourcompany.ontapgiuaky.service.impl;
import com.yourcompany.ontapgiuaky.entity.Course;
import com.yourcompany.ontapgiuaky.entity.CourseCategory;
import com.yourcompany.ontapgiuaky.entity.CourseImage;
import com.yourcompany.ontapgiuaky.repository.CourseCategoryRepository;
import com.yourcompany.ontapgiuaky.repository.CourseImageRepository;
import com.yourcompany.ontapgiuaky.repository.CourseRepository;
import com.yourcompany.ontapgiuaky.service.CourseService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Service
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CourseCategoryRepository courseCategoryRepository;
    private final CourseImageRepository courseImageRepository;
    public CourseServiceImpl(CourseRepository courseRepository,
                            CourseCategoryRepository courseCategoryRepository,
                            CourseImageRepository courseImageRepository) {
        this.courseRepository = courseRepository;
        this.courseCategoryRepository = courseCategoryRepository;
        this.courseImageRepository = courseImageRepository;
    }
    @Override
    public Course findById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }
    @Override
    @Transactional
    public Course saveCourse(Course course) {
        validateCourseCategory(course.getCategory());
        validateDuplicateCourseName(course.getCategory().getId(), course.getName(), null);
        return courseRepository.save(course);
    }
    @Override
    @Transactional
    public Course updateCourse(Long id, Course course) {
        Course existingCourse = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));
        validateCourseCategory(course.getCategory());
        validateDuplicateCourseName(course.getCategory().getId(), course.getName(), id);
        existingCourse.setName(course.getName());
        existingCourse.setTuitionFee(course.getTuitionFee());
        existingCourse.setDuration(course.getDuration());
        existingCourse.setIsActive(course.getIsActive());
        existingCourse.setDescription(course.getDescription());
        existingCourse.setCategory(course.getCategory());
        return courseRepository.save(existingCourse);
    }
    @Override
    @Transactional
    public void deleteCourseCategory(Long categoryId) {
        CourseCategory category = courseCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Loại khóa học không tồn tại"));
        if (courseRepository.existsByCategoryId(categoryId)) {
            throw new IllegalArgumentException("Không thể xóa loại khóa học đang có dữ liệu");
        }
        courseCategoryRepository.delete(category);
    }
    @Override
    @Transactional
    public CourseImage addCourseImage(Long courseId, String imageUrl) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("URL ảnh không được để trống");
        }
        CourseImage courseImage = new CourseImage();
        courseImage.setCourse(course);
        courseImage.setImageUrl(imageUrl.trim());
        courseImage.setIsThumbnail(false);
        return courseImageRepository.save(courseImage);
    }
    @Override
    @Transactional
    public void setThumbnail(Long courseId, Long imageId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));
        CourseImage selectedImage = courseImageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Ảnh không tồn tại"));
        if (!Objects.equals(selectedImage.getCourse().getId(), courseId)) {
            throw new IllegalArgumentException("Ảnh không thuộc khóa học này");
        }
        List<CourseImage> images = course.getImages() == null ? new ArrayList<>() : course.getImages();
        CourseImage oldThumbnail = images.stream()
                .filter(image -> Boolean.TRUE.equals(image.getIsThumbnail()))
                .findFirst()
                .orElse(null);
        if (oldThumbnail != null && !Objects.equals(oldThumbnail.getId(), imageId)) {
            oldThumbnail.setIsThumbnail(false);
            courseImageRepository.save(oldThumbnail);
        }
        selectedImage.setIsThumbnail(true);
        courseImageRepository.save(selectedImage);
    }
    @Override
    public Page<Course> searchCourses(String keyword, Long categoryId, Boolean isActive,
                                      Double minFee, Double maxFee, Pageable pageable) {
        if (minFee != null && maxFee != null && minFee > maxFee) {
            throw new IllegalArgumentException("Học phí tối thiểu không được lớn hơn học phí tối đa");
        }
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }
        return courseRepository.searchCourses(keyword, categoryId, isActive, minFee, maxFee, pageable);
    }
    private void validateCourseCategory(CourseCategory category) {
        if (category == null || category.getId() == null) {
            throw new IllegalArgumentException("Phải chọn loại khóa học");
        }
        if (!courseCategoryRepository.existsById(category.getId())) {
            throw new IllegalArgumentException("Loại khóa học không tồn tại");
        }
    }
    private void validateDuplicateCourseName(Long categoryId, String name, Long excludeCourseId) {
        if (name == null || name.trim().isEmpty()) {
            return;
        }
        boolean exists = courseRepository.existsByCategoryIdAndNameIgnoreCaseAndIdNot(categoryId, name.trim(), excludeCourseId);
        if (exists) {
            throw new IllegalArgumentException("Tên khóa học đã tồn tại trong loại khóa học này");
        }
    }
}
