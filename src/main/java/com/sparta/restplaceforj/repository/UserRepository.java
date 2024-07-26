package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findByEmail(String email);

  Optional<User> findByNickname(String nickname);

  default void findByEmailOrThrow(String email) {
    Optional<User> userOptional = findByEmail(email);
    userOptional.ifPresent(user -> {
      throw new CommonException(ErrorEnum.DUPLICATED_EMAIL);
    });
  }

  default void findByNicknameOrThrow(String nickname) {
    Optional<User> userOptional = findByNickname(nickname);
    userOptional.ifPresent(user -> {
      throw new CommonException(ErrorEnum.DUPLICATED_NICKNAME);
    });
  }
}
