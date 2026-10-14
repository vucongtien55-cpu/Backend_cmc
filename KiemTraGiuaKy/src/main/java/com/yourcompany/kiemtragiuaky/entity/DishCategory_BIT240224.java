package com.yourcompany.kiemtragiuaky.entity;

import com.google.errorprone.annotations.NoAllocation;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BIT240224_DishCategories")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DishCategory_BIT240224 {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BIT240224_Id")
    private Long id;

    @NotBlank(message = "Category name is required")
    @Column(name = "BIT240224_Name", nullable = false)
    private String name;

    @Column(name = "BIT240224_Description", length = 1000)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "dishCategory")
    private List<Dish_BIT240224> dishes = new ArrayList<>();

}

