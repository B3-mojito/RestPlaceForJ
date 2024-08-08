package com.sparta.restplaceforj.service;

import com.sparta.restplaceforj.dto.AddCardRequestDto;
import com.sparta.restplaceforj.dto.PageResponseDto;
import com.sparta.restplaceforj.dto.PostIdTitleDto;
import com.sparta.restplaceforj.dto.PostRequestDto;
import com.sparta.restplaceforj.dto.PostResponseDto;
import com.sparta.restplaceforj.entity.Card;
import com.sparta.restplaceforj.entity.Column;
import com.sparta.restplaceforj.entity.Image;
import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.RelatedPost;
import com.sparta.restplaceforj.entity.ThemeEnum;
import com.sparta.restplaceforj.entity.User;
import com.sparta.restplaceforj.exception.CommonException;
import com.sparta.restplaceforj.exception.ErrorEnum;
import com.sparta.restplaceforj.repository.CardRepository;
import com.sparta.restplaceforj.repository.ColumnRepository;
import com.sparta.restplaceforj.repository.ImageRepository;
import com.sparta.restplaceforj.repository.PlanRepository;
import com.sparta.restplaceforj.repository.PostDslRepository;
import com.sparta.restplaceforj.repository.PostRepository;
import com.sparta.restplaceforj.repository.RelatedPostRepository;
import com.sparta.restplaceforj.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
  private final CardRepository cardRepository;
  private final RelatedPostRepository relatedPostRepository;
  private final PlanRepository planRepository;
  private final ColumnRepository columnRepository;

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

  /**
   * 카드 연관 게시물 추가
   *
   * @param
   * @param postId            포스트 아이디
   * @param addCardRequestDto 저장할 데이터 id, address, memo
   * @return CardResponseDto List<PlanResponseDto> : planId, title
   */
  @Transactional
  public PostResponseDto cardAddPost(Long postId, AddCardRequestDto addCardRequestDto) {
    Long cardId = addCardRequestDto.getCardId();
    if (cardId != null) {
      Card card = cardRepository.findCardById(cardId);
      Post post = postRepository.findByIdOrThrow(postId);

      if (relatedPostRepository.findPostsByCardId(cardId).equals(post)) {
        throw new CommonException(ErrorEnum.BAD_REQUEST);
      }
      RelatedPost cardPost = RelatedPost.builder()
          .card(card)
          .post(post)
          .build();
      relatedPostRepository.save(cardPost);
      return PostResponseDto.builder()
          .post(post)
          .build();
    }
    Column column = columnRepository.findByPlanIdAndTitle(addCardRequestDto.getPlanId(), "미정");
    Post post = postRepository.findByIdOrThrow(postId);
    Card card = Card.builder()
        .column(column)
        .title(addCardRequestDto.getPlaceName())
        .address(post.getAddress())
        .placeName(post.getPlaceName())
        .startedAt(addCardRequestDto.getStartedAt())
        .endedAt(addCardRequestDto.getEndedAt())
        .memo(addCardRequestDto.getMemo())
        .build();
    cardRepository.save(card);
    return PostResponseDto.builder()
        .post(post)
        .build();
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
