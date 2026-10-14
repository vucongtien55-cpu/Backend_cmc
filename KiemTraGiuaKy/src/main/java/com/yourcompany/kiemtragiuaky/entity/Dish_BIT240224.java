package com.yourcompany.kiemtragiuaky.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "BIT240224_Dishes",
        uniqueConstraints = @UniqueConstraint(name = "uk_dish_name_category", columnNames = {"BIT240224_Name", "BIT240224_DishCategoryId"}))
public class Dish_BIT240224 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BIT240224_id")
    private Long id;

    @NotBlank(message = "Dish name is required")
    @Column(name = "BIT240224_Name", nullable = false)
    private String name;

    @NotNull(message = "Price is required") @Positive(message = "Price must be greater than 0")
    @Column(name = "BIT240224_Price", nullable = false)
    private Double price;

    @NotNull(message = "Preparation time is required") @Positive(message = "Preparation time must be greater than 0")
    @Column(name = "BIT240224_PreparationTime", nullable = false)
    private Integer preparationTime;

    @Column(name = "BIT240224_IsAvailable", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "BIT240224_Description", length = 2000)
    private String description;

    @NotNull(message = "Please select a category")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BIT240224_DishCategoryId", nullable = false)
    private DishCategory_BIT240224 dishCategory;

    @JsonIgnore
    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DishImage_BIT240224> images = new ArrayList<>();

    @Transient
    public String getPreparationType() {
        return preparationTime != null && preparationTime <= 15 ? "Quick / Nhanh" : "Regular / Thông thường";
    }

}

