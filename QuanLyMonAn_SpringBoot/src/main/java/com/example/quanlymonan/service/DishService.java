package com.example.quanlymonan.service;

import com.example.quanlymonan.entity.*;
import com.example.quanlymonan.repository.*;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.*;

@Service
public class DishService {
    private final DishRepository dishRepo;
    private final DishCategoryRepository categoryRepo;
    private final DishImageRepository imageRepo;

    public DishService(DishRepository dishRepo, DishCategoryRepository categoryRepo, DishImageRepository imageRepo) {
        this.dishRepo = dishRepo; this.categoryRepo = categoryRepo; this.imageRepo = imageRepo;
    }

    public List<Dish_MSSV_CUA_BAN> search(String name, Long categoryId, Boolean available,
                                           Double minPrice, Double maxPrice, String sortBy, String direction) {
        if (minPrice != null && minPrice < 0 || maxPrice != null && maxPrice < 0 ||
            minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid price range");
        }
        Specification<Dish_MSSV_CUA_BAN> spec = (root, query, cb) -> {
            List<Predicate> p = new ArrayList<>();
            if (name != null && !name.isBlank()) p.add(cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%"));
            if (categoryId != null) p.add(cb.equal(root.get("dishCategory").get("id"), categoryId));
            if (available != null) p.add(cb.equal(root.get("isAvailable"), available));
            if (minPrice != null) p.add(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            if (maxPrice != null) p.add(cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            return cb.and(p.toArray(new Predicate[0]));
        };
        String field = Set.of("price", "preparationTime", "name").contains(sortBy) ? sortBy : "id";
        Sort.Direction dir = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
        return dishRepo.findAll(spec, Sort.by(dir, field));
    }

    public Dish_MSSV_CUA_BAN getDish(Long id) {
        return dishRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found"));
    }

    @Transactional
    public Dish_MSSV_CUA_BAN saveDish(Dish_MSSV_CUA_BAN input, Long categoryId, Long id) {
        var category = categoryRepo.findById(categoryId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish category not found"));
        boolean duplicate = id == null
            ? dishRepo.existsByNameIgnoreCaseAndDishCategoryId(input.getName().trim(), categoryId)
            : dishRepo.existsByNameIgnoreCaseAndDishCategoryIdAndIdNot(input.getName().trim(), categoryId, id);
        if (duplicate) throw new ResponseStatusException(HttpStatus.CONFLICT, "Dish name already exists in this category");
        Dish_MSSV_CUA_BAN dish = id == null ? new Dish_MSSV_CUA_BAN() : getDish(id);
        dish.setName(input.getName().trim());
        dish.setPrice(input.getPrice());
        dish.setPreparationTime(input.getPreparationTime());
        dish.setIsAvailable(input.getIsAvailable() == null ? true : input.getIsAvailable());
        dish.setDescription(input.getDescription());
        dish.setDishCategory(category);
        return dishRepo.save(dish);
    }

    public void deleteDish(Long id) {
        if (!dishRepo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found");
        dishRepo.deleteById(id);
    }

    public List<DishCategory_MSSV_CUA_BAN> categories() { return categoryRepo.findAll(Sort.by("name")); }

    public DishCategory_MSSV_CUA_BAN saveCategory(DishCategory_MSSV_CUA_BAN category) {
        return categoryRepo.save(category);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        if (dishRepo.existsByDishCategoryId(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete a category used by dishes");
        categoryRepo.deleteById(id);
    }

    @Transactional
    public DishImage_MSSV_CUA_BAN addImage(Long dishId, String url, boolean thumbnail) {
        Dish_MSSV_CUA_BAN dish = getDish(dishId);
        if (thumbnail) imageRepo.findByDishId(dishId).forEach(img -> img.setIsThumbnail(false));
        DishImage_MSSV_CUA_BAN image = new DishImage_MSSV_CUA_BAN();
        image.setImageUrl(url); image.setIsThumbnail(thumbnail); image.setDish(dish);
        return imageRepo.save(image);
    }

    @Transactional
    public DishImage_MSSV_CUA_BAN setThumbnail(Long imageId) {
        DishImage_MSSV_CUA_BAN selected = imageRepo.findById(imageId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
        imageRepo.findByDishId(selected.getDish().getId()).forEach(img -> img.setIsThumbnail(false));
        selected.setIsThumbnail(true);
        return imageRepo.save(selected);
    }

    public void deleteImage(Long id) {
        if (!imageRepo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found");
        imageRepo.deleteById(id);
    }
}
