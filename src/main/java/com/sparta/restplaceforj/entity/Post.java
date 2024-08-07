package com.sparta.restplaceforj.entity;

import com.sparta.restplaceforj.dto.PostRequestDto;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@DynamicUpdate
@Getter
@Table(name = "posts")
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  private String title;

  private String content;

  private String address;

  private String placeName;

  private long likesCount;

  private long viewsCount;

  @Enumerated(EnumType.STRING)
  private ThemeEnum themeEnum;

  @OneToMany(mappedBy = "post")
  private List<Image> imageList;

  @Builder
  public Post(PostRequestDto requestDto, User user) {
    this.user = user;
    this.title = requestDto.getTitle();
    this.content = requestDto.getContent();
    this.address = requestDto.getAddress();
    this.themeEnum = ThemeEnum.valueOf(requestDto.getTheme());
    this.placeName = requestDto.getPlaceName();
    this.imageList = new ArrayList<>();
  }

  public void update(PostRequestDto postRequestDto) {
    if (postRequestDto.getTitle() != null) {
      title = postRequestDto.getTitle();
    }
    if (postRequestDto.getContent() != null) {
      content = postRequestDto.getContent();
    }
    if (postRequestDto.getAddress() != null) {
      address = postRequestDto.getAddress();
    }
    if (postRequestDto.getTheme() != null) {
      themeEnum = ThemeEnum.valueOf(postRequestDto.getTheme());
    }
    if (postRequestDto.getPlaceName() != null) {
      placeName = postRequestDto.getPlaceName();
    }
  }

  public void removeLikeFromPost() {
    likesCount--;
  }

  public void addLikeToPost() {
    likesCount++;
  }

  public void addViewToPost() {
    viewsCount++;
  }

  //양방향 추가 메서드
  public void addImages(Image image) {
    imageList.add(image);
    image.setPost(this);
  }
}
