package com.example.nagoyameshi.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import com.example.nagoyameshi.entity.base.BaseTimeEntity;

/**
 * 利用規約を表すエンティティクラス。
 * データベースの terms テーブルとマッピングされる。
 */
@Entity
@Table(name = "terms")
@Data
@EqualsAndHashCode(callSuper = false) // タイムスタンプは等価判定に含めない
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Term extends BaseTimeEntity {
    /** ID */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 利用規約本文（HTML形式など） */
    private String content;
}
