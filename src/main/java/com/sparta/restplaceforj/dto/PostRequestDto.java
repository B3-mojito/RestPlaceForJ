package com.sparta.restplaceforj.dto;

import com.sparta.restplaceforj.entity.ThemaEnum;

import lombok.Getter;

@Getter
public class PostRequestDto {

	private String title;

	private String content;

	private String address;

	private ThemaEnum themaEnum;
}
