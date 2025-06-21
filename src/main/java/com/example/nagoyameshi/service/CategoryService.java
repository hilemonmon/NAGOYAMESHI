package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.nagoyameshi.entity.Category;
import com.example.nagoyameshi.form.CategoryEditForm;
import com.example.nagoyameshi.form.CategoryRegisterForm;

/**
 * カテゴリ情報を操作するサービスインターフェース。
 */
public interface CategoryService {

    /** 全カテゴリをページ取得する。 */
    Page<Category> findAllCategories(Pageable pageable);

    /**
     * キーワードで部分一致検索する。
     *
     * @param keyword  検索ワード
     * @param pageable ページ情報
     * @return 検索結果ページ
     */
    Page<Category> findCategoriesByNameLike(String keyword, Pageable pageable);

    /** ID を指定してカテゴリを取得する。 */
    Optional<Category> findCategoryById(Long id);

    /** 登録されているカテゴリ数を数える。 */
    long countCategories();

    /** 最後に登録されたカテゴリを取得する。 */
    Optional<Category> findFirstCategoryByOrderByIdDesc();

    /** カテゴリを登録する。 */
    Category createCategory(CategoryRegisterForm form);

    /** カテゴリを更新する。 */
    Category updateCategory(Long id, CategoryEditForm form);

    /** カテゴリを削除する。 */
    void deleteCategory(Long id);
}
