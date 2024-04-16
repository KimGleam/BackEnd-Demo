package com.shop.doubleu.product.repository;

import com.shop.doubleu.product.dto.CategoryDTO;
import com.shop.doubleu.product.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {

    List<CategoryDTO> findBypCode(String pCode);
}
