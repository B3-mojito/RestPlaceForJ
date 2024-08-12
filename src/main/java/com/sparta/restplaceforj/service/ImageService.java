package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.ImageResponseDto;
import com.sparta.restplaceforj.entity.Image;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.provider.ImageProvider;
import com.sparta.restplaceforj.repository.ImageRepository;
import com.sparta.restplaceforj.s3.S3Service;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ImageService {

  private final ImageRepository imageRepository;
  private final S3Service s3Service;
  private final ImageProvider imageProvider;

  /**
   * 사진 s3에 저장
   *
   * @param multipartFile 저장할 사진
   * @return ImageResponseDto : id, path, originalFileName, changedFileName
   * @throws IOException s3Service.upload 생길 수 있는 예외
   */
  @Transactional
  public ImageResponseDto createPostImage(MultipartFile multipartFile) throws IOException {

    //원본 이름
    String originalFileName = multipartFile.getOriginalFilename();
    //파일 확장자 추출
    String ext = originalFileName.substring(originalFileName.lastIndexOf("."));

    imageProvider.imageCheck(ext);

    // 중복을 방지하기위한 S3 저장하는 이름
    String changedFileName = imageProvider.changeImageName(ext);
    //저장 후 경로
    String path = s3Service.upload(multipartFile, changedFileName);

    Image image = Image.builder()
        .originalFileName(originalFileName)
        .changedFileName(changedFileName)
        .path(path)
        .build();

    imageRepository.save(image);

    return ImageResponseDto.builder()
        .image(image)
        .build();
  }


  /**
   * 사진 조회.
   *
   * @param imageId 조회할 사진 아이디
   * @return ImageResponseDto : id, path, originalFileName, changedFileName
   */
  public ImageResponseDto getImage(long imageId) {
    Image image = imageRepository.findByIdOrThrow(imageId);
    return ImageResponseDto.builder()
        .image(image)
        .build();
  }

  /**
   * 사진 삭제
   *
   * @param imageId 사진 아이디
   */
  @Transactional
  public void deleteImage(long imageId) {
    if (!imageRepository.existsById(imageId)) {
      throw new CommonException(ErrorEnum.IMAGE_NOT_FOUND);
    }
    imageRepository.deleteById(imageId);
  }

  /*
    일정 시간이 지난 후 연관관계가 없는 이미지는 자동으로 삭제
                     초 분 시 일 월 요일  -> 오전 12시 마다 실행*/
  @Scheduled(cron = "0 0 0 * * *")
  @Transactional
  public void deleteUnNecessaryImage() {
    log.info(new Date() + "스케쥴러 실행");
    List<Image> images = imageRepository.findByPostIsNull();
    List<Image> deletedImageList = s3Service.deleteUnNecessaryImage(images);
    imageRepository.deleteAll(deletedImageList);

  }

}
