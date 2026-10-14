package com.example.quanlymonan.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "MSSV_CUA_BAN_DishCategories")
public class DishCategory_MSSV_CUA_BAN {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MSSV_CUA_BAN_Id")
    private Long id;

    @NotBlank(message = "Category name is required")
    @Column(name = "MSSV_CUA_BAN_Name", nullable = false)
    private String name;

    @Column(name = "MSSV_CUA_BAN_Description", length = 1000)
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "dishCategory")
    private List<Dish_MSSV_CUA_BAN> dishes = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Dish_MSSV_CUA_BAN> getDishes() { return dishes; }
    public void setDishes(List<Dish_MSSV_CUA_BAN> dishes) { this.dishes = dishes; }
}
