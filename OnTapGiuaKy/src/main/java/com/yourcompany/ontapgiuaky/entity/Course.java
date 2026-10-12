package com.yourcompany.ontapgiuaky.entity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Tên khóa học không được để trống")
    @Column(nullable = false)
    private String name;
    @NotNull(message = "Học phí không được để trống")
    @Min(value = 1, message = "Học phí phải lớn hơn 0")
    @Column(nullable = false)
    private Double tuitionFee;
    @NotNull(message = "Thời lượng không được để trống")
    @Min(value = 1, message = "Thời lượng phải lớn hơn 0")
    @Column(nullable = false)
    private Integer duration;
    @Column(nullable = false)
    private Boolean isActive;
    @Column(columnDefinition = "TEXT")
    private String description;
    @NotNull(message = "Phải chọn loại khóa học")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    private CourseCategory category;
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CourseImage> images = new ArrayList<>();
    @Transient
    public String getDurationType() {
        if (duration == null) {
            return null;
        }
        return duration <= 30 ? "Ngắn hạn" : "Dài hạn";
    }
}
