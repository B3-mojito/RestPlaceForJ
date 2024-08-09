package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickname(String nickname);

    Optional<User> findByEmail(String email);

    default User findByEmailOrThrow(String email) {
        return findByEmail(email).orElseThrow(() -> new CommonException(ErrorEnum.USER_NOT_FOUND));
    }

    default User findByIdOrThrow(Long userId) {
        return findById(userId).orElseThrow(() -> new CommonException(ErrorEnum.USER_NOT_FOUND));
    }

    Optional<User> findByKakaoId(Long kakaoId);
}
