package com.yourcompany.kiemtragiuaky.service;


import com.yourcompany.kiemtragiuaky.entity.DishCategory_BIT240224;
import com.yourcompany.kiemtragiuaky.entity.DishImage_BIT240224;
import com.yourcompany.kiemtragiuaky.entity.Dish_BIT240224;
import com.yourcompany.kiemtragiuaky.repository.DishCategoryRepository;
import com.yourcompany.kiemtragiuaky.repository.DishImageRepository;
import com.yourcompany.kiemtragiuaky.repository.DishRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DishService {
    private final DishRepository dishRepo;
    private final DishCategoryRepository categoryRepo;
    private final DishImageRepository imageRepo;


    public List<Dish_BIT240224> search(String name, Long categoryId, Boolean available,
                                          Double minPrice, Double maxPrice, String sortBy, String direction) {
        if (minPrice != null && minPrice < 0 || maxPrice != null && maxPrice < 0 ||
                minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid price range");
        }
        Specification<Dish_BIT240224> spec = (root, query, cb) -> {
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

    public Dish_BIT240224 getDish(Long id) {
        return dishRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dish not found"));
    }

    @Transactional
    public Dish_BIT240224 saveDish(Dish_BIT240224 input, Long categoryId, Long id) {
        var category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Dish category not found"));
        boolean duplicate = id == null
                ? dishRepo.existsByNameIgnoreCaseAndDishCategoryId(input.getName().trim(), categoryId)
                : dishRepo.existsByNameIgnoreCaseAndDishCategoryIdAndIdNot(input.getName().trim(), categoryId, id);
        if (duplicate) throw new ResponseStatusException(HttpStatus.CONFLICT, "Dish name already exists in this category");
        Dish_BIT240224 dish = id == null ? new Dish_BIT240224() : getDish(id);
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

    public List<DishCategory_BIT240224> categories() { return categoryRepo.findAll(Sort.by("name")); }

    public DishCategory_BIT240224 saveCategory(DishCategory_BIT240224 category) {
        return categoryRepo.save(category);
    }

    public void deleteCategory(Long id) {
        if (!categoryRepo.existsById(id)) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found");
        if (dishRepo.existsByDishCategoryId(id)) throw new ResponseStatusException(HttpStatus.CONFLICT, "Cannot delete a category used by dishes");
        categoryRepo.deleteById(id);
    }

    @Transactional
    public DishImage_BIT240224 addImage(Long dishId, String url, boolean thumbnail) {
        Dish_BIT240224 dish = getDish(dishId);
        if (thumbnail) imageRepo.findByDishId(dishId).forEach(img -> img.setIsThumbnail(false));
        DishImage_BIT240224 image = new DishImage_BIT240224();
        image.setImageUrl(url); image.setIsThumbnail(thumbnail); image.setDish(dish);
        return imageRepo.save(image);
    }

    @Transactional
    public DishImage_BIT240224 setThumbnail(Long imageId) {
        DishImage_BIT240224 selected = imageRepo.findById(imageId)
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

