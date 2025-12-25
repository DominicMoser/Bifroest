package com.dmoser.codyssey.bifroest.banner;

import com.dmoser.codyssey.bifroest.session.AppConfig;

public class SimpleContextNameBanner implements Banner {

  public SimpleContextNameBanner() {}

  @Override
  public String getString() {
    return AppConfig.get().appName();
  }
}
