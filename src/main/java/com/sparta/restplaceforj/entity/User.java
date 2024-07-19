package com.sparta.restplaceforj.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nickname;
    private String email;
    private String password;
    private String bio;
    private String picture;
    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;
}
