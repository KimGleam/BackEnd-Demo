package com.shop.doubleu.product.service;

import com.shop.doubleu.product.dto.CategoryDTO;
import com.shop.doubleu.product.entity.Category;
import com.shop.doubleu.product.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getAllCategoryList () {
        return categoryRepository.findAll();
    }

    public List<CategoryDTO> getParentCategoryList (String pCode) {
        return categoryRepository.findBypCode(pCode);
    }
}
