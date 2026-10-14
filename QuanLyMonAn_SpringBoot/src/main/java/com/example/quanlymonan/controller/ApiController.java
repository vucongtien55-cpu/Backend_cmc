package com.example.quanlymonan.controller;

import com.example.quanlymonan.entity.*;
import com.example.quanlymonan.repository.DishImageRepository;
import com.example.quanlymonan.service.DishService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
@Validated
public class ApiController {
    private final DishService service;
    private final DishImageRepository imageRepo;
    public ApiController(DishService service, DishImageRepository imageRepo) { this.service = service; this.imageRepo = imageRepo; }

    @GetMapping("/dishes")
    public List<Dish_MSSV_CUA_BAN> dishes(@RequestParam(required=false) String name,
        @RequestParam(required=false) Long categoryId, @RequestParam(required=false) Boolean available,
        @RequestParam(required=false) Double minPrice, @RequestParam(required=false) Double maxPrice,
        @RequestParam(defaultValue="id") String sortBy, @RequestParam(defaultValue="asc") String direction) {
        return service.search(name, categoryId, available, minPrice, maxPrice, sortBy, direction);
    }

    @GetMapping("/dishes/{id}") public Dish_MSSV_CUA_BAN dish(@PathVariable Long id) { return service.getDish(id); }

    @PostMapping("/dishes")
    public Dish_MSSV_CUA_BAN create(@Valid @RequestBody DishRequest req) {
        Dish_MSSV_CUA_BAN d = req.toDish();
        return service.saveDish(d, req.categoryId(), null);
    }

    @PutMapping("/dishes/{id}")
    public Dish_MSSV_CUA_BAN update(@PathVariable Long id, @Valid @RequestBody DishRequest req) {
        return service.saveDish(req.toDish(), req.categoryId(), id);
    }

    @DeleteMapping("/dishes/{id}") public Map<String,String> deleteDish(@PathVariable Long id) {
        service.deleteDish(id); return Map.of("message", "Deleted");
    }

    @GetMapping("/categories") public List<DishCategory_MSSV_CUA_BAN> categories() { return service.categories(); }

    @PostMapping("/categories") public DishCategory_MSSV_CUA_BAN createCategory(@Valid @RequestBody CategoryRequest req) {
        DishCategory_MSSV_CUA_BAN c = new DishCategory_MSSV_CUA_BAN(); c.setName(req.name()); c.setDescription(req.description());
        return service.saveCategory(c);
    }

    @DeleteMapping("/categories/{id}") public Map<String,String> deleteCategory(@PathVariable Long id) {
        service.deleteCategory(id); return Map.of("message", "Deleted");
    }

    @GetMapping("/dishes/{dishId}/images")
    public List<DishImage_MSSV_CUA_BAN> images(@PathVariable Long dishId) {
        service.getDish(dishId); return imageRepo.findByDishId(dishId);
    }

    @PostMapping("/dishes/{dishId}/images")
    public DishImage_MSSV_CUA_BAN addImage(@PathVariable Long dishId, @Valid @RequestBody ImageRequest req) {
        return service.addImage(dishId, req.imageUrl(), Boolean.TRUE.equals(req.isThumbnail()));
    }

    @PutMapping("/images/{imageId}/thumbnail")
    public DishImage_MSSV_CUA_BAN thumbnail(@PathVariable Long imageId) { return service.setThumbnail(imageId); }

    @DeleteMapping("/images/{imageId}") public Map<String,String> deleteImage(@PathVariable Long imageId) {
        service.deleteImage(imageId); return Map.of("message", "Deleted");
    }

    public record DishRequest(@NotBlank String name, @jakarta.validation.constraints.NotNull @jakarta.validation.constraints.Positive Double price,
        @jakarta.validation.constraints.NotNull @jakarta.validation.constraints.Positive Integer preparationTime,
        Boolean isAvailable, String description, @jakarta.validation.constraints.NotNull Long categoryId) {
        Dish_MSSV_CUA_BAN toDish() {
            Dish_MSSV_CUA_BAN d = new Dish_MSSV_CUA_BAN(); d.setName(name); d.setPrice(price);
            d.setPreparationTime(preparationTime); d.setIsAvailable(isAvailable); d.setDescription(description); return d;
        }
    }
    public record CategoryRequest(@NotBlank String name, String description) {}
    public record ImageRequest(@NotBlank String imageUrl, Boolean isThumbnail) {}
}
