package com.sparta.restplaceforj.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

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
  private String picture;
  @Enumerated(value = EnumType.STRING)
  private UserStatus userStatus;
  private LocalDateTime authUserAt;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Setter
    private String refreshToken;

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
}
