package com.yourcompany.kiemtragiuaky.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BIT240224_DishImages")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DishImage_BIT240224 {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BIT240224_Id")
    private Long id;

    @NotBlank(message = "Image URL is required")
    @Column(name = "BIT240224_ImageUrl", nullable = false, length = 2000)
    private String imageUrl;

    @Column(name = "BIT240224_IsThumbnail", nullable = false)
    private Boolean isThumbnail = false;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "BIT240224_DishId", nullable = false)
    private Dish_BIT240224 dish;

}

