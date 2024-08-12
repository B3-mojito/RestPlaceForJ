package com.sparta.restplaceforj.provider;

import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j(topic = "ImageProvider")
public class ImageProvider {

  /**
   * 사진 파일만 업로드 가능하게 이미지 파일 체크
   *
   * @param ext 확장자 명
   */
  public void imageCheck(String ext) {
    if (!(ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".png"))) {
      throw new CommonException(ErrorEnum.ONLY_IMAGE);
    }
  }

  //uuid 를 이용해서 중복없는 이름 만든다.
  public String changeImageName(String ext) {
    final String uuid = UUID.randomUUID().toString();
    return uuid + ext;
  }

}
