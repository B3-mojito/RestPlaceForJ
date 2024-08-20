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
  private Long id;

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

  private Long kakaoId;


  @Builder
  public User(String nickname, String name, String email, String password) {
    this.nickname = nickname;
    this.name = name;
    this.email = email;
    this.password = password;
    this.userRole = UserRole.USER;
    this.userStatus = UserStatus.ACTIVE;
  }

  public User(String nickname, String name, String email, String password, Long kakaoId) {
    this.nickname = nickname;
    this.name = name;
    this.email = email;
    this.password = password;
    this.userRole = UserRole.USER;
    this.userStatus = UserStatus.ACTIVE;
    this.kakaoId = kakaoId;
  }

  public void setUserStatusDeactivate(UserStatus userStatus) {
    this.userStatus = userStatus;
    this.authUserAt = LocalDateTime.now();
  }

  public void updateProfile(String nickname, String bio) {
    this.nickname = nickname;
    this.bio = bio;
  }

  public void updatePassword(String password) {
    this.password = password;
  }

  public User updateKakao(Long kakaoId) {
    this.kakaoId = kakaoId;
    return this;
  }
}
