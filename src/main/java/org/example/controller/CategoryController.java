package org.example.controller;


import org.example.dto.category.CategoryCreateResponseDTO;
import org.example.dto.category.CategoryDTO;
import org.example.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/admin/create")
    public ResponseEntity<CategoryCreateResponseDTO> create(@RequestBody CategoryDTO category) {
        CategoryCreateResponseDTO categoryCreateResponseDTO = categoryService.create(category);
        return  ResponseEntity.ok().body(categoryCreateResponseDTO);
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<Boolean> update(@PathVariable Integer id,@RequestBody CategoryDTO dto){
        return  ResponseEntity.ok().body(categoryService.update(id,dto));

    }

    @DeleteMapping("/admin/delete/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id) {
        return ResponseEntity.ok().body(categoryService.delete(id));
    }

    @GetMapping("/getCategoryList")
    public ResponseEntity<List<CategoryCreateResponseDTO>> getList(){
        return ResponseEntity.ok().body(categoryService.getList());
    }
}
