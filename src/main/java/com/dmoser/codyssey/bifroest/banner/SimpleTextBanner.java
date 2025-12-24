package com.dmoser.codyssey.bifroest.banner;

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
