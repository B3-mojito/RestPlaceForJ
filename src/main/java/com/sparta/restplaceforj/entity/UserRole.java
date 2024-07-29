package com.sparta.restplaceforj.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
  USER("USER");

  private final String roleValue;
}

