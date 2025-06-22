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

@Entity
@Table(name = "companies")
@Data
@EqualsAndHashCode(callSuper = false) // タイムスタンプは等価判定に含めない
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Company extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String postalCode;

    private String address;

    private String representative;

    /** 創業年月日 */
    private String establishmentDate;

    private String capital;

    private String business;

    private String numberOfEmployees;
}
