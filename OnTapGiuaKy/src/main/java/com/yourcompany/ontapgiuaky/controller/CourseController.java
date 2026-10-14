package com.yourcompany.ontapgiuaky.controller;
import com.yourcompany.ontapgiuaky.entity.Course;
import com.yourcompany.ontapgiuaky.entity.CourseCategory;
import com.yourcompany.ontapgiuaky.entity.CourseImage;
import com.yourcompany.ontapgiuaky.repository.CourseCategoryRepository;
import com.yourcompany.ontapgiuaky.repository.CourseImageRepository;
import com.yourcompany.ontapgiuaky.repository.CourseRepository;
import com.yourcompany.ontapgiuaky.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
@Controller
@RequestMapping("/")
public class CourseController {
    private final CourseService courseService;
    private final CourseRepository courseRepository;
    private final CourseCategoryRepository courseCategoryRepository;
    private final CourseImageRepository courseImageRepository;
    public CourseController(CourseService courseService,
                           CourseRepository courseRepository,
                           CourseCategoryRepository courseCategoryRepository,
                           CourseImageRepository courseImageRepository) {
        this.courseService = courseService;
        this.courseRepository = courseRepository;
        this.courseCategoryRepository = courseCategoryRepository;
        this.courseImageRepository = courseImageRepository;
    }
    @GetMapping("/courses")
    public String getCourses(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(required = false) Double minFee,
            @RequestParam(required = false) Double maxFee,
            @RequestParam(defaultValue = "tuitionFee") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Model model
    ) {
        String safeSortBy = (sortBy == null || sortBy.isBlank()) ? "tuitionFee" : sortBy;
        Sort.Direction direction = "asc".equalsIgnoreCase(sortDir) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, safeSortBy);
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        Page<Course> courses = courseService.searchCourses(keyword, categoryId, isActive, minFee, maxFee, pageable);
        List<CourseCategory> categories = courseCategoryRepository.findAll();
        model.addAttribute("courses", courses.getContent());
        model.addAttribute("categories", categories);
        model.addAttribute("courseDto", new Course());
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("isActive", isActive);
        model.addAttribute("minFee", minFee);
        model.addAttribute("maxFee", maxFee);
        model.addAttribute("sortBy", safeSortBy);
        model.addAttribute("sortDir", sortDir);
        return "courses";
    }
    @GetMapping("/courses/{id}")
    public String getCourseById(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        Course course = courseRepository.findById(id).orElse(null);
        if (course == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Khóa học không tồn tại");
            return "redirect:/courses";
        }
        model.addAttribute("course", course);
        return "course-detail";
    }
    @PostMapping("/courses/save")
    public String saveCourse(@Valid @ModelAttribute("courseDto") Course course,
                             BindingResult result,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("categories", courseCategoryRepository.findAll());
            model.addAttribute("courses", courseService.searchCourses(null, null, null, null, null,
                    PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "tuitionFee"))).getContent());
            model.addAttribute("errorMessage", "Dữ liệu không hợp lệ");
            return "courses";
        }
        try {
            courseService.saveCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Lưu khóa học thành công");
            return "redirect:/courses";
        } catch (IllegalArgumentException e) {
            model.addAttribute("categories", courseCategoryRepository.findAll());
            model.addAttribute("courses", courseService.searchCourses(null, null, null, null, null,
                    PageRequest.of(0, Integer.MAX_VALUE, Sort.by(Sort.Direction.DESC, "tuitionFee"))).getContent());
            model.addAttribute("errorMessage", e.getMessage());
            return "courses";
        }
    }
    @PostMapping("/courses/{id}/add-image")
    public String addCourseImage(@PathVariable Long id,
                                 @RequestParam String imageUrl,
                                 RedirectAttributes redirectAttributes) {
        try {
            courseService.addCourseImage(id, imageUrl);
            redirectAttributes.addFlashAttribute("successMessage", "Thêm ảnh thành công");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/courses/" + id;
    }
    @PostMapping("/courses/images/{imageId}/set-thumbnail")
    public String setThumbnail(@PathVariable Long imageId, RedirectAttributes redirectAttributes) {
        try {
            CourseImage image = courseImageRepository.findById(imageId)
                    .orElseThrow(() -> new IllegalArgumentException("Ảnh không tồn tại"));
            courseService.setThumbnail(image.getCourse().getId(), imageId);
            redirectAttributes.addFlashAttribute("successMessage", "Đã đặt ảnh đại diện thành công");
            return "redirect:/courses/" + image.getCourse().getId();
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/courses";
        }
    }

}
