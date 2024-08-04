package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.ImageResponseDto;
import com.sparta.restplaceforj.dto.PageResponseDto;
import com.sparta.restplaceforj.dto.PostIdTitleDto;
import com.sparta.restplaceforj.dto.PostRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.entity.Image;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.ThemeEnum;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.ImageRepository;
import com.sparta.restplaceforj.repository.PostDslRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import com.sparta.restplaceforj.s3.S3Service;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * 추천글 서비스.
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostService {

  private final PostRepository postRepository;
  private final PostDslRepository postDslRepository;
  private final ImageRepository imageRepository;
  private final S3Service s3Service;

  /**
   * 글 생성
   *
   * @param postRequestDto : title, content, address, theme, placeName
   * @param user           : 토큰 유저 정보
   * @return PostResponseDto : title, content, address, likesCount, viewCount, themeEnum
   */
  @Transactional
  public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {

    try {
      ThemeEnum.valueOf(postRequestDto.getTheme());
    } catch (IllegalArgumentException e) {
      throw new CommonException(ErrorEnum.THEME_NOT_FOUND);
    }

    Post post = postRepository.save(
        Post.builder()
            .requestDto(postRequestDto)
            .user(user)
            .build());

    if (!postRequestDto.getImageIdList().isEmpty()) {
      for (Long imageId : postRequestDto.getImageIdList()) {
        Image image = imageRepository.findByIdOrThrow(imageId);
        post.addImages(image);
      }
    }

    Post savedPost = postRepository.save(post);

    return PostResponseDto.builder()
        .post(savedPost)
        .build();
  }

  /**
   * 삭제
   *
   * @param postId pathVariable
   * @param user   : 토큰 유저 정보
   */
  @Transactional
  public void deletePost(long postId, User user) {
    Post post = postRepository.findByIdOrThrow(postId);
    if (post.getUser().getId() != user.getId()) {
      throw new CommonException(ErrorEnum.POST_MISMATCH);
    }

    postRepository.deleteById(postId);
  }

  public PageResponseDto<String> getPlaceList(
      int page, int size, String region, String theme) {

    ThemeEnum themeEnum = ThemeEnum.valueOf(theme);
    Pageable pageRequest = PageRequest.of(page, size);

    PageImpl<String> placeNameList = postDslRepository
        .getPostListGroupByPlaceName(pageRequest, region, themeEnum);

    return PageResponseDto.<String>builder()
        .page(placeNameList)
        .build();
  }

  /**
   * 글을 조회하면 제목과 아이디만 반환  .
   *
   * @param page      현재 페이지
   * @param size      페이지 크기
   * @param placeName 장소명
   * @param q         검색질문
   * @param sortBy    정렬 기준
   * @return PageResponseDto : placeNameList, size, page, totalPages, totalElements
   */
  public PageResponseDto<PostIdTitleDto> getPostTitleList(
      int page, int size, String placeName, String sortBy, String q) {

    sortByCheck(sortBy);

    Sort sort = Sort.by(Direction.DESC, sortBy);

    Pageable pageRequest = PageRequest.of(page, size, sort);

    PageImpl<PostIdTitleDto> postIdTitleList = postDslRepository
        .getPostTitleList(pageRequest, placeName, q);

    return PageResponseDto.<PostIdTitleDto>builder()
        .page(postIdTitleList)
        .build();
  }

  /**
   * 글 수정
   *
   * @param postId         수정 글 아이디
   * @param postRequestDto title, content, address, theme, placeName
   * @param user           : 토큰 유저 정보
   * @return PostResponseDto id, userId, title, content, address, likesCount, viewsCount, themeEnum
   */
  @Transactional
  public PostResponseDto updatePost(long postId, PostRequestDto postRequestDto, User user) {
    Post post = postRepository.findByIdOrThrow(postId);
    if (post.getUser().getId() != user.getId()) {
      throw new CommonException(ErrorEnum.POST_MISMATCH);
    }

    post.update(postRequestDto);
    return PostResponseDto.builder()
        .post(post)
        .build();
  }

  /**
   * 단권글 조회.
   *
   * @param postId 조회 글 아이디
   * @return PostResponseDto :id, userId, title, content, address, likesCount, viewsCount, themeEnum
   */
  @Transactional
  public PostResponseDto getPost(long postId) {
    Post post = postRepository.findByIdOrThrow(postId);
    post.addViewToPost();

    return PostResponseDto.builder()
        .post(post)
        .build();
  }

  @Transactional
  public ImageResponseDto createPostImage(MultipartFile multipartFile)
      throws IOException {

    //원본 이름
    String originalFileName = multipartFile.getOriginalFilename();
    //파일 확장자 추출
    String ext = originalFileName.substring(originalFileName.lastIndexOf("."));

    imageCheck(ext);

    // 중복을 방지하기위한 S3 저장하는 이름
    String changedFileName = changeImageName(ext);
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

  private static void imageCheck(String ext) {
    if (!(ext.equals(".jpg") || ext.equals(".jpeg") || ext.equals(".png"))) {
      throw new CommonException(ErrorEnum.ONLY_IMAGE);
    }
  }

  //uuid 를 이용해서 중복없는 이름 만든다.
  private String changeImageName(String ext) {
    final String uuid = UUID.randomUUID().toString();
    return uuid + ext;
  }

  /*
    일정 시간이 지난 후 연관관계가 없는 이미지는 자동으로 삭제
                     초 분 시 일 월 요일
    @Scheduled(cron = "0 40 14 * * *") -> 매일 오후 2시
    */
  @Scheduled(cron = "0 0 0 * * *")
  @Transactional
  public void deleteUnNecessaryImage() {
    log.info(new Date() + "스케쥴러 실행");
    List<Image> images = imageRepository.findByPostIsNull();
    List<Image> deletedImageList = s3Service.deleteUnNecessaryImage(images);
    imageRepository.deleteAll(deletedImageList);

  }

  public PageResponseDto<PostIdTitleDto> getMyPostList(
      int page, int size, String sortBy, long userId) {
    sortByCheck(sortBy);

    Sort sort = Sort.by(Direction.DESC, sortBy);

    Pageable pageRequest = PageRequest.of(page, size, sort);

    PageImpl<PostIdTitleDto> postIdTitleList = postDslRepository
        .getMyPostList(pageRequest, userId);

    return PageResponseDto.<PostIdTitleDto>builder()
        .page(postIdTitleList)
        .build();
  }

  private static void sortByCheck(String sortBy) {
    if (!(sortBy.equals("createdAt") || sortBy.equals("viewsCount") ||
        sortBy.equals("likesCount"))) {
      throw new CommonException(ErrorEnum.SORT_NOT_FOUND);
    }
  }
}
