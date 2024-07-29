package com.sparta.restplaceforj.entity;

import lombok.Getter;

@Getter
public enum ThemeEnum {
  HEALING("healing"),
  THRILL("thrill"),
  CAMPING("camping"),
  ACTIVITIES("activities"),
  FOOD_TOUR("food tour"),
  SHOPPING("shopping"),
  CULTURAL("cultural"),
  MARKET(" market"),
  NATURE("nature"),
  EXPERIENCE("experience");

  private final String theme;

  ThemeEnum(String theme) {
    this.theme = theme;
  }
}
