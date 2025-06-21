package com.example.nagoyameshi.entity;

import java.time.LocalDate;

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
import com.example.nagoyameshi.entity.base.BaseTimeEntity;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String furigana;

    private String postalCode;

    private String address;

    private String phoneNumber;

    private LocalDate birthday;

    private String occupation;
  
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)

    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    private boolean enabled;

    private String stripeCustomerId;
}
