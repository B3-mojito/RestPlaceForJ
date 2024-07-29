package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.Post;
import com.sparta.restplaceforj.entity.ThemaEnum;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostResponseDto {

    private final long id;
    // private final long userId;
    private final String title;
    private final String content;
    private final String address;
    private final long like_count;
    private final long view_count;
    private final ThemaEnum themeEnum;

    @Builder
    public PostResponseDto(Post post) {
        this.id = post.getId();
        // this.userId = post.getUser().getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.address = post.getAddress();
        this.like_count = post.getLike_count();
        this.view_count = post.getView_count();
        this.themeEnum = post.getThemeEnum();
    }
}
