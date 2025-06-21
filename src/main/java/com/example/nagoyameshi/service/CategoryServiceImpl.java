package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.form.CategoryEditForm;
import com.example.nagoyameshi.form.CategoryRegisterForm;
import com.example.nagoyameshi.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

/**
 * {@link CategoryService} の実装クラス。
 * データベース操作は {@link CategoryRepository} を利用する。
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    /** データベースアクセス用リポジトリ */
    private final CategoryRepository categoryRepository;

    /** {@inheritDoc} */
    @Override
    public Page<Category> findAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Page<Category> findCategoriesByNameLike(String keyword, Pageable pageable) {
        return categoryRepository.findByNameContaining(keyword, pageable);
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Category> findCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    /** {@inheritDoc} */
    @Override
    public long countCategories() {
        return categoryRepository.count();
    }

    /** {@inheritDoc} */
    @Override
    public Optional<Category> findFirstCategoryByOrderByIdDesc() {
        return categoryRepository.findFirstByOrderByIdDesc();
    }

    /** {@inheritDoc} */
    @Override
    public Category createCategory(CategoryRegisterForm form) {
        Category category = Category.builder()
                .name(form.getName())
                .build();
        return categoryRepository.save(category);
    }

    /** {@inheritDoc} */
    @Override
    public Category updateCategory(Long id, CategoryEditForm form) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));
        category.setName(form.getName());
        return categoryRepository.save(category);
    }

    /** {@inheritDoc} */
    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
