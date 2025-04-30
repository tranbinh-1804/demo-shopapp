package com.tranbinh.demo_shopapp.services;

import com.tranbinh.demo_shopapp.dtos.CategoryDTO;
import com.tranbinh.demo_shopapp.entities.Category;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;
import com.tranbinh.demo_shopapp.repositories.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;

    // Helper method to map Category to CategoryDTO entity
    private CategoryDTO mapCategoryToDTO(Category category){
        return CategoryDTO.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    // Helper method to map CategoryDTO to Category entity
    private Category mapDTOToCategory(CategoryDTO categoryDTO){
        return Category.builder()
                .id(categoryDTO.getId())
                .name(categoryDTO.getName())
                .build();
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
        if(categoryDTO.getId() != null){
            throw new IllegalArgumentException("Category ID must be null when creating a new category");
        }

        Category newCategory = Category.builder()
                .name(categoryDTO.getName()).build();
        Category savedCategory = categoryRepository.save(newCategory);
        return mapCategoryToDTO(savedCategory);
    }

    @Override
    public CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) throws DataNotFoundException {
        Category existingCategory = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category not found with id: " + id));
        existingCategory.setName(categoryDTO.getName());
        Category updatedCategory = categoryRepository.save(existingCategory);
        return mapCategoryToDTO(updatedCategory);
    }

    @Override
    public void deleteCategory(Long id) throws DataNotFoundException {
        if(!categoryRepository.existsById(id)){
            throw new DataNotFoundException("Category not found with id: " + id);
        }
        categoryRepository.deleteById(id);
    }

    @Override
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(this::mapCategoryToDTO)
                .toList();
    }

    @Override
    public CategoryDTO getCategoryById(Long id) throws DataNotFoundException {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Category not found"));
        return mapCategoryToDTO(category);
    }
}
