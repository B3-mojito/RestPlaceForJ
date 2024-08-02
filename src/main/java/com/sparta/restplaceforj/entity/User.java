package com.sparta.restplaceforj.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String nickname;

    private String name;

    private String email;

    private String password;

    private String bio;

    @Setter
    private String profileImage;

    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;

    private LocalDateTime authUserAt;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;


    @Builder
    public User(String nickname, String name, String email, String password) {
        this.nickname = nickname;
        this.name = name;
        this.email = email;
        this.password = password;
        this.userRole = UserRole.USER;
        this.userStatus = UserStatus.ACTIVE;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
        this.authUserAt = LocalDateTime.now();
    }
    public void updateProfile(String nickname, String bio, String password) {
        this.nickname = nickname;
        this.bio = bio;
        this.password = password;
    }
}
