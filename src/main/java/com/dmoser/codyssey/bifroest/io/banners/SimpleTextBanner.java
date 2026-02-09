package com.dmoser.codyssey.bifroest.io.banners;

import com.dmoser.codyssey.bifroest.io.Banner;

public class SimpleTextBanner implements Banner {

  private final String text;

  public SimpleTextBanner(String text) {
    this.text = text;
  }

  @Override
  public String getString() {
    return text;
  }
}
