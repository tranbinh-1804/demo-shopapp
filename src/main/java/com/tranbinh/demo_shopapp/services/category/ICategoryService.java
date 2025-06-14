package com.tranbinh.demo_shopapp.services.category;

import com.tranbinh.demo_shopapp.dtos.CategoryDTO;
import com.tranbinh.demo_shopapp.entities.Category;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;

import java.util.List;

public interface ICategoryService {

    Category createCategory(CategoryDTO categoryDTO);

    Category updateCategory(Long id, CategoryDTO categoryDTO) throws DataNotFoundException;

    void deleteCategory(Long id) throws DataNotFoundException;

    List<Category> getAllCategories();

    Category getCategoryById(Long id) throws DataNotFoundException;
}