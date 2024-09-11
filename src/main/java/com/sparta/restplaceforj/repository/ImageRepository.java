package com.sparta.restplaceforj.repository;

import com.sparta.restplaceforj.entity.Image;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

  List<Image> findByPostIsNull();

  default Image findByIdOrThrow(long imageId) {
    return findById(imageId).orElseThrow(
        () -> new CommonException(ErrorEnum.IMAGE_NOT_FOUND)
    );
  }

  Image findByPostId(long postId);
}
