package com.sparta.restplaceforj.controller;

import com.sparta.restplaceforj.common.CommonResponse;
import com.sparta.restplaceforj.common.ResponseEnum;
import com.sparta.restplaceforj.dto.ImageResponseDto;
import com.sparta.restplaceforj.service.ImageService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/images")
public class ImageController {

  private final ImageService imageService;

  /**
   * 사진을 s3 업로드 api.
   *
   * @param images 저장할 파일
   * @return ImageResponseDto : id, path, originalFileName, changedFiledName
   * @throws IOException InputStream getInputStream() throws IOException
   */
  @PostMapping
  public ResponseEntity<CommonResponse<ImageResponseDto>> createPostImage(
      @RequestPart("images") MultipartFile images) throws IOException {

    ImageResponseDto imageResponseDto = imageService.createPostImage(images);

    return ResponseEntity.ok(
        CommonResponse.<ImageResponseDto>builder()
            .response(ResponseEnum.CREATE_IMAGE)
            .data(imageResponseDto)
            .build()
    );
  }

  /**
   * 사진 조회 api.
   *
   * @param postId 조회할 사진 아이디
   * @return ImageResponseDto : id, path, originalFileName, changedFiledName
   */
  @GetMapping("/{post-id}")
  public ResponseEntity<CommonResponse<ImageResponseDto>> getImage(
      @PathVariable("post-id") long postId) {

    ImageResponseDto imageResponseDto = imageService.getImage(postId);

    return ResponseEntity.ok(
        CommonResponse.<ImageResponseDto>builder()
            .response(ResponseEnum.GET_IMAGE)
            .data(imageResponseDto)
            .build()
    );
  }

  /**
   * 사진 삭제 api.
   *
   * @param imageId 사진 아이디
   * @return data : null
   */
  @DeleteMapping("/{image-id}")
  public ResponseEntity<CommonResponse> deleteImage(
      @PathVariable("image-id") long imageId) {

    imageService.deleteImage(imageId);

    return ResponseEntity.ok(
        CommonResponse.builder()
            .response(ResponseEnum.DELETE_IMAGE)
            .build()
    );
  }

}
