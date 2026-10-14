package com.example.quanlymonan.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MSSV_CUA_BAN_Dishes",
       uniqueConstraints = @UniqueConstraint(name = "uk_dish_name_category", columnNames = {"MSSV_CUA_BAN_Name", "MSSV_CUA_BAN_DishCategoryId"}))
public class Dish_MSSV_CUA_BAN {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MSSV_CUA_BAN_Id")
    private Long id;

    @NotBlank(message = "Dish name is required")
    @Column(name = "MSSV_CUA_BAN_Name", nullable = false)
    private String name;

    @NotNull(message = "Price is required") @Positive(message = "Price must be greater than 0")
    @Column(name = "MSSV_CUA_BAN_Price", nullable = false)
    private Double price;

    @NotNull(message = "Preparation time is required") @Positive(message = "Preparation time must be greater than 0")
    @Column(name = "MSSV_CUA_BAN_PreparationTime", nullable = false)
    private Integer preparationTime;

    @Column(name = "MSSV_CUA_BAN_IsAvailable", nullable = false)
    private Boolean isAvailable = true;

    @Column(name = "MSSV_CUA_BAN_Description", length = 2000)
    private String description;

    @NotNull(message = "Please select a category")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MSSV_CUA_BAN_DishCategoryId", nullable = false)
    private DishCategory_MSSV_CUA_BAN dishCategory;

    @JsonIgnore
    @OneToMany(mappedBy = "dish", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DishImage_MSSV_CUA_BAN> images = new ArrayList<>();

    @Transient
    public String getPreparationType() {
        return preparationTime != null && preparationTime <= 15 ? "Quick / Nhanh" : "Regular / Thông thường";
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }
    public Integer getPreparationTime() { return preparationTime; }
    public void setPreparationTime(Integer preparationTime) { this.preparationTime = preparationTime; }
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean available) { isAvailable = available; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public DishCategory_MSSV_CUA_BAN getDishCategory() { return dishCategory; }
    public void setDishCategory(DishCategory_MSSV_CUA_BAN dishCategory) { this.dishCategory = dishCategory; }
    public List<DishImage_MSSV_CUA_BAN> getImages() { return images; }
    public void setImages(List<DishImage_MSSV_CUA_BAN> images) { this.images = images; }
}
