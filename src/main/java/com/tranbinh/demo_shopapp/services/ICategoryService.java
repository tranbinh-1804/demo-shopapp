package com.tranbinh.demo_shopapp.services;

import com.tranbinh.demo_shopapp.dtos.CategoryDTO;
import com.tranbinh.demo_shopapp.entities.Category;
import com.tranbinh.demo_shopapp.exceptions.DataNotFoundException;

import java.util.List;

public interface ICategoryService {

    CategoryDTO createCategory(CategoryDTO categoryDTO);

    CategoryDTO updateCategory(Long id, CategoryDTO categoryDTO) throws DataNotFoundException;

    void deleteCategory(Long id) throws DataNotFoundException;

    List<CategoryDTO> getAllCategories();

    CategoryDTO getCategoryById(Long id) throws DataNotFoundException;
}