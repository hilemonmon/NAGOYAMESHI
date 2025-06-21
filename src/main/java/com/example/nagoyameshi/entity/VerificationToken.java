package com.example.nagoyameshi.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.example.nagoyameshi.entity.base.BaseTimestampEntity;

@Entity
@Table(name = "verification_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VerificationToken extends BaseTimestampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 主キー。IDENTITY方式で自動採番される
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id") // users テーブルの主キーを参照
    private User user;

    // 認証用に発行したトークンを保持
    @Column(nullable = false, unique = true)
    private String token;
}
