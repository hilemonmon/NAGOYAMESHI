package com.example.nagoyameshi.entity.base;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * java.sql.Timestamp を使うエンティティ向けの基底クラス。
 * テーブルの created_at / updated_at を自動管理する。
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseTimestampEntity {

    /** 作成日時。INSERT 時に自動設定される */
    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    /** 更新日時。INSERT/UPDATE 時に自動設定される */
    @Column(name = "updated_at", nullable = false)
    private Timestamp updatedAt;

    /** 登録前に作成・更新日時を設定する */
    @PrePersist
    public void prePersist() {
        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        this.createdAt = now;
        this.updatedAt = now;
    }

    /** 更新前に更新日時のみ変更する */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = Timestamp.valueOf(LocalDateTime.now());
    }
}
