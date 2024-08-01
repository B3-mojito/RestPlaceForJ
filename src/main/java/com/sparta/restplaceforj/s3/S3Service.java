package com.sparta.restplaceforj.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.restplaceforj.entity.Image;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@Service
public class S3Service {

  @Value("${cloud.aws.s3.bucket}")
  private String bucket;

  private final AmazonS3 amazonS3;

  public String upload(MultipartFile multipartFile) throws IOException {
    String s3FileName = UUID.randomUUID() + "-" + multipartFile.getOriginalFilename();

    ObjectMetadata objMeta = new ObjectMetadata();
    objMeta.setContentLength(multipartFile.getInputStream().available());

    amazonS3.putObject(bucket, s3FileName, multipartFile.getInputStream(), objMeta);

    return amazonS3.getUrl(bucket, s3FileName).toString();
  }

  public String upload(MultipartFile multipartFile, String changedImageName)
      throws IOException {

    ObjectMetadata objMeta = new ObjectMetadata();
    objMeta.setContentLength(multipartFile.getInputStream().available());

    amazonS3.putObject(bucket, changedImageName, multipartFile.getInputStream(), objMeta);

    return amazonS3.getUrl(bucket, changedImageName).toString();
  }

  public List<Image> deleteUnNecessaryImage(List<Image> images) {
    List<Image> imagesToDeleteList = new ArrayList<>();
    images.stream()
        .filter(
            image -> Duration.between(image.getCreatedAt(), LocalDateTime.now()).toHours() >= 1)
        .forEach(image -> {
          DeleteObjectRequest deleteRequest = new DeleteObjectRequest(
              bucket, image.getChangedFileName());
          amazonS3.deleteObject(deleteRequest);
          imagesToDeleteList.add(image);
        });

    return imagesToDeleteList;

  }

}
