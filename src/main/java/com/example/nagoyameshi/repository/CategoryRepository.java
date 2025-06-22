package com.example.nagoyameshi.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.nagoyameshi.entity.Category;

/**
 * カテゴリ情報へアクセスするリポジトリ。
 */
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * 名前で部分一致検索を行い、ページング結果を返します。
     *
     * @param name     検索キーワード
     * @param pageable ページ情報
     * @return 取得したカテゴリページ
     */
    Page<Category> findByNameContaining(String name, Pageable pageable);

    /**
     * ID の降順で最初のレコードを取得します。
     *
     * @return もっとも新しいカテゴリ
     */
    Optional<Category> findFirstByOrderByIdDesc();

    /**
     * 指定された名前を持つ最初のカテゴリを取得します。
     *
     * @param name カテゴリ名
     * @return 名前に一致するカテゴリ（最初の1件）
     */
    Optional<Category> findFirstByName(String name);
}
