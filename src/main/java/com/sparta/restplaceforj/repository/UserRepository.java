package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
