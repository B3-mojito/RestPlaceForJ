package com.sparta.restplaceforj.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ImageRequestDto {

  MultipartFile images;

}
