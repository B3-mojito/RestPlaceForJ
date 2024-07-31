package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Image;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

  List<Image> findByPostIsNull();
}
