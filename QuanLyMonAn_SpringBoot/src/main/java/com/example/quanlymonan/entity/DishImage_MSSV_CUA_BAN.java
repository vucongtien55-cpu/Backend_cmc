package com.example.quanlymonan.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "MSSV_CUA_BAN_DishImages")
public class DishImage_MSSV_CUA_BAN {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MSSV_CUA_BAN_Id")
    private Long id;

    @NotBlank(message = "Image URL is required")
    @Column(name = "MSSV_CUA_BAN_ImageUrl", nullable = false, length = 2000)
    private String imageUrl;

    @Column(name = "MSSV_CUA_BAN_IsThumbnail", nullable = false)
    private Boolean isThumbnail = false;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "MSSV_CUA_BAN_DishId", nullable = false)
    private Dish_MSSV_CUA_BAN dish;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public Boolean getIsThumbnail() { return isThumbnail; }
    public void setIsThumbnail(Boolean thumbnail) { isThumbnail = thumbnail; }
    public Dish_MSSV_CUA_BAN getDish() { return dish; }
    public void setDish(Dish_MSSV_CUA_BAN dish) { this.dish = dish; }
}
