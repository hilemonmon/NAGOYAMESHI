package com.example.nagoyameshi.entity.base;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import lombok.Getter;
import lombok.Setter;

/**
 * エンティティの作成日時と更新日時を自動管理する基底クラス。
 * 他のエンティティはこのクラスを継承することで同じ仕組みを利用できる。
 */
@MappedSuperclass
@Getter
@Setter
public abstract class BaseTimeEntity {

    /** 作成日時。初回INSERT時に自動で設定される */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /** 更新日時。INSERT時とUPDATE時に自動で設定される */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 新規レコード登録前に呼び出され、作成日時と更新日時をセットする。
     */
    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }

    /**
     * 既存レコード更新前に呼び出され、更新日時のみを変更する。
     */
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
